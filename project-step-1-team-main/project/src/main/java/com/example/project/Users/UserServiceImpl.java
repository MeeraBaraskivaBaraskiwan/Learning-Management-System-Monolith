package com.example.project.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserModelAssembler userAssembler;
    private final PagedResourcesAssembler<UserDTO> pagedAssembler;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserMapper userMapper,
                           UserModelAssembler userAssembler,
                           PagedResourcesAssembler<UserDTO> pagedAssembler,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.userAssembler = userAssembler;
        this.pagedAssembler = pagedAssembler;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PagedModel<EntityModel<UserDTO>> getAllUsers(Pageable pageable) {
        logger.info("Fetching all users with pageable: {}", pageable);
        Page<UserDTO> userPage = userRepository.findAll(pageable)
                .map(userMapper::toDTO);
        logger.info("Successfully fetched {} users", userPage.getTotalElements());
        return pagedAssembler.toModel(userPage, userAssembler);
    }

    @Override
    public ResponseEntity<?> newUser(UserDTO newUserDTO) {
        logger.info("Creating a new user with email: {}", newUserDTO.getEmail());
        if (userRepository.findByEmail(newUserDTO.getEmail()).isPresent()) {
            logger.warn("User with email {} already exists", newUserDTO.getEmail());
            throw new AlreadyExistsException("User with email " + newUserDTO.getEmail() + " already exists.");
        }

        String roleName = newUserDTO.getRoleName();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> {
                    logger.error("Role with name {} not found", roleName);
                    return new ResourceNotFoundException("Role with name " + roleName + " not found");
                });

        String encodedPassword = passwordEncoder.encode(newUserDTO.getPassword());
        newUserDTO.setPassword(encodedPassword);
        User user = userMapper.toEntity(newUserDTO, role);

        try {
            user = userRepository.save(user);
            logger.info("Successfully created user with ID: {}", user.getId());
        } catch (DataIntegrityViolationException ex) {
            logger.error("Failed to create user due to data integrity violation: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already in use or invalid data: " + ex.getMessage());
        }

        UserDTO savedDTO = userMapper.toDTO(user);
        EntityModel<UserDTO> entityModel = userAssembler.toModel(savedDTO);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public EntityModel<UserDTO> getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", id);
                    return new ResourceNotFoundException("User with ID " + id + " not found");
                });
        logger.info("Successfully fetched user with ID: {}", id);
        return userAssembler.toModel(userMapper.toDTO(user));
    }

    @Override
    public ResponseEntity<?> updateUser(Long id, UserDTO updatedUserDTO) {
        logger.info("Updating user with ID: {}", id);
        return userRepository.findById(id).map(user -> {
            if (!user.getEmail().equals(updatedUserDTO.getEmail()) &&
                userRepository.findByEmail(updatedUserDTO.getEmail()).isPresent()) {
                logger.warn("Email {} is already in use", updatedUserDTO.getEmail());
                return ResponseEntity.badRequest()
                        .body("Email " + updatedUserDTO.getEmail() + " is already in use.");
            }

            user.setEmail(updatedUserDTO.getEmail());
            user.setFirstName(updatedUserDTO.getFirstName());
            user.setLastName(updatedUserDTO.getLastName());
            user.setPassword(updatedUserDTO.getPassword());

            String encodedPassword = passwordEncoder.encode(updatedUserDTO.getPassword());
            user.setPassword(encodedPassword);

            String roleName = updatedUserDTO.getRoleName();
            Role newRole = roleRepository.findByName(roleName)
                    .orElseThrow(() -> {
                        logger.error("Role with name {} not found", roleName);
                        return new ResourceNotFoundException("Role with name " + roleName + " not found");
                    });
            user.setRole(newRole);

            try {
                userRepository.save(user);
                logger.info("Successfully updated user with ID: {}", id);
            } catch (DataIntegrityViolationException ex) {
                logger.error("Failed to update user due to data integrity violation: {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid data: " + ex.getMessage());
            }

            EntityModel<UserDTO> entityModel = userAssembler.toModel(userMapper.toDTO(user));
            return ResponseEntity.ok(entityModel);
        }).orElseThrow(() -> {
            logger.error("User with ID {} not found", id);
            return new ResourceNotFoundException("User with ID " + id + " not found");
        });
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("User with ID {} not found", id);
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
        logger.info("Successfully deleted user with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}