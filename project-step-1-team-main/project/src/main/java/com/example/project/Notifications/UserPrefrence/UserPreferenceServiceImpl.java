package com.example.project.Notifications.UserPrefrence;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.web.PagedResourcesAssembler;
import com.example.project.Users.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;
    private final UserPreferenceAssembler assembler;
    private final PagedResourcesAssembler<UserPreferenceDTO> pagedAssembler;

    public UserPreferenceServiceImpl(
            UserPreferenceRepository userPreferenceRepository,
            UserRepository userRepository,
            UserPreferenceAssembler assembler,
            PagedResourcesAssembler<UserPreferenceDTO> pagedAssembler
    ) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<UserPreferenceDTO>> createPreference(UserPreferenceDTO dto) {
        logger.info("Creating user preference for user ID: {}", dto.getUserId());
        if (dto.getId() != null) {
            logger.error("ID should not be provided when creating a new preference");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must not be provided when creating a new preference");
        }
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", dto.getUserId());
                    return new ResourceNotFoundException("User with ID " + dto.getUserId() + " not found");
                });
        UserPreference preference = UserPreferenceMapper.toEntity(dto, user);
        UserPreference saved = userPreferenceRepository.save(preference);
        logger.info("User preference created with ID: {}", saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(UserPreferenceMapper.toDTO(saved)));
    }

    @Override
    public EntityModel<UserPreferenceDTO> getPreferenceById(Long id) {
        logger.info("Retrieving user preference with ID: {}", id);
        UserPreference preference = userPreferenceRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("UserPreference with ID {} not found", id);
                    return new ResourceNotFoundException("UserPreference with ID " + id + " not found");
                });
        logger.info("User preference with ID {} retrieved successfully", id);
        return assembler.toModel(UserPreferenceMapper.toDTO(preference));
    }

    @Override
    public EntityModel<UserPreferenceDTO> getPreferenceByUserId(Long userId) {
        logger.info("Retrieving user preference for user ID: {}", userId);
        UserPreference preference = userPreferenceRepository.findByUser_Id(userId)
                .orElseThrow(() -> {
                    logger.error("No preference found for user ID {}", userId);
                    return new ResourceNotFoundException("No preference found for user ID " + userId);
                });
        logger.info("User preference for user ID {} retrieved successfully", userId);
        return assembler.toModel(UserPreferenceMapper.toDTO(preference));
    }

    @Override
    public PagedModel<EntityModel<UserPreferenceDTO>> getAllPreferences(Pageable pageable) {
        logger.info("Retrieving all user preferences with pageable: {}", pageable);
        Page<UserPreferenceDTO> page = userPreferenceRepository.findAll(pageable)
                .map(UserPreferenceMapper::toDTO);
        logger.info("Retrieved {} user preferences", page.getTotalElements());
        return pagedAssembler.toModel(page, assembler);
    }

    @Override
    public PagedModel<EntityModel<UserPreferenceDTO>> getAllPreferencesNoArgs() {
        logger.info("Retrieving all user preferences without pagination");
        return getAllPreferences(Pageable.unpaged());
    }

    @Override
    public ResponseEntity<EntityModel<UserPreferenceDTO>> updatePreference(Long id, UserPreferenceDTO dto) {
        logger.info("Attempting to update user preference with ID: {}", id);
        return userPreferenceRepository.findById(id).map(pref -> {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User with ID {} not found", dto.getUserId());
                        return new ResourceNotFoundException("User with ID " + dto.getUserId() + " not found");
                    });
            pref.setUser(user);
            pref.setEmailEnabled(dto.isEmailEnabled());
            pref.setSmsEnabled(dto.isSmsEnabled());
            pref.setPhoneNumber(dto.getPhoneNumber());
            UserPreference updated = userPreferenceRepository.save(pref);
            logger.info("User preference with ID {} updated successfully", id);
            return ResponseEntity.ok(assembler.toModel(UserPreferenceMapper.toDTO(updated)));
        }).orElseThrow(() -> {
            logger.error("UserPreference with ID {} not found for update", id);
            return new ResourceNotFoundException("UserPreference with ID " + id + " not found");
        });
    }

    @Override
    public ResponseEntity<?> deletePreference(Long id) {
        logger.info("Attempting to delete user preference with ID: {}", id);
        return userPreferenceRepository.findById(id).map(pref -> {
            userPreferenceRepository.delete(pref);
            logger.info("User preference with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> {
            logger.error("UserPreference with ID {} not found for deletion", id);
            return new ResourceNotFoundException("UserPreference with ID " + id + " not found");
        });
    }
}