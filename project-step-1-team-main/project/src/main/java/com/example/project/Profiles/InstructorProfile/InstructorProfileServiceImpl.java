package com.example.project.Profiles.InstructorProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Instructors.Instructor;
import com.example.project.Instructors.InstructorRepository;
import com.example.project.Profiles.Profile.Profile;
import com.example.project.Profiles.Profile.ProfileRepository;
import com.example.project.Users.UserRepository;

@Service
public class InstructorProfileServiceImpl implements InstructorProfileService {

    private static final Logger logger = LoggerFactory.getLogger(InstructorProfileServiceImpl.class);

    private final InstructorProfileRepository instructorProfileRepository;
    private final ProfileRepository profileRepository;
    private final InstructorProfileMapper instructorProfileMapper;
    private final InstructorProfileAssembler instructorProfileAssembler;
    private final PagedResourcesAssembler<InstructorProfileDTO> pagedAssembler;
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;

    public InstructorProfileServiceImpl(InstructorProfileRepository instructorProfileRepository,
                                        ProfileRepository profileRepository,
                                        InstructorProfileMapper instructorProfileMapper,
                                        InstructorProfileAssembler instructorProfileAssembler,
                                        PagedResourcesAssembler<InstructorProfileDTO> pagedAssembler,
                                        InstructorRepository instructorRepository,
                                        UserRepository userRepository) {
        this.instructorProfileRepository = instructorProfileRepository;
        this.profileRepository = profileRepository;
        this.instructorProfileMapper = instructorProfileMapper;
        this.instructorProfileAssembler = instructorProfileAssembler;
        this.pagedAssembler = pagedAssembler;
        this.instructorRepository = instructorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<EntityModel<InstructorProfileDTO>> createInstructorProfile(InstructorProfileDTO dto) {
        logger.info("Creating a new instructor profile for instructorId: {}", dto.getInstructorId());

        if (instructorProfileRepository.findByInstructorId(dto.getInstructorId()).isPresent()) {
            logger.warn("Instructor profile already exists for instructorId: {}", dto.getInstructorId());
            throw new AlreadyExistsException("Instructor profile already exists for instructor id " + dto.getInstructorId());
        }

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> {
                    logger.error("Profile with ID: {} not found", dto.getProfileId());
                    return new ResourceNotFoundException("Profile with ID " + dto.getProfileId() + " not found");
                });

        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> {
                    logger.error("Instructor with ID: {} not found", dto.getInstructorId());
                    return new ResourceNotFoundException("Instructor with ID " + dto.getInstructorId() + " not found");
                });

        InstructorProfile entity = instructorProfileMapper.toEntity(dto, profile, instructor);
        InstructorProfile saved = instructorProfileRepository.save(entity);
        logger.info("Successfully created a new instructor profile for instructorId: {}", dto.getInstructorId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(instructorProfileAssembler.toModel(instructorProfileMapper.toDTO(saved)));
    }

    @Override
    public EntityModel<InstructorProfileDTO> getInstructorProfileById(Long id) {
        logger.info("Fetching instructor profile with ID: {}", id);
        InstructorProfile entity = instructorProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("InstructorProfile with ID: {} not found", id);
                    return new ResourceNotFoundException("InstructorProfile with ID " + id + " not found");
                });
        logger.info("Successfully fetched instructor profile with ID: {}", id);
        return instructorProfileAssembler.toModel(instructorProfileMapper.toDTO(entity));
    }

    @Override
    public EntityModel<InstructorProfileDTO> getInstructorProfileByInstructorId(Long instructorId) {
        logger.info("Fetching instructor profile for instructorId: {}", instructorId);
        InstructorProfile entity = instructorProfileRepository.findByInstructorId(instructorId)
                .orElseThrow(() -> {
                    logger.error("InstructorProfile not found for instructorId: {}", instructorId);
                    return new ResourceNotFoundException("InstructorProfile not found for instructorId " + instructorId);
                });
        logger.info("Successfully fetched instructor profile for instructorId: {}", instructorId);
        return instructorProfileAssembler.toModel(instructorProfileMapper.toDTO(entity));
    }

    @Override
    public PagedModel<EntityModel<InstructorProfileDTO>> getAllInstructorProfiles(Pageable pageable) {
        logger.info("Fetching all instructor profiles with pageable: {}", pageable);
        PagedModel<EntityModel<InstructorProfileDTO>> profiles = pagedAssembler.toModel(
                instructorProfileRepository.findAll(pageable).map(instructorProfileMapper::toDTO),
                instructorProfileAssembler);
        logger.info("Successfully fetched all instructor profiles");
        return profiles;
    }

    @Override
    public ResponseEntity<EntityModel<InstructorProfileDTO>> updateInstructorProfile(Long id, InstructorProfileDTO dto, Authentication authentication) {
        logger.info("Updating instructor profile with ID: {}", id);

        InstructorProfile entity = instructorProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("InstructorProfile with ID: {} not found", id);
                    return new ResourceNotFoundException("InstructorProfile with ID " + id + " not found");
                });

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Long profileInstructorId = entity.getInstructor().getUser().getId();
            Long currentUserId = userRepository.findByEmail(username)
                    .orElseThrow(() -> {
                        logger.error("Authenticated user not found for username: {}", username);
                        return new ResourceNotFoundException("Authenticated user not found");
                    }).getId();

            if (!profileInstructorId.equals(currentUserId)) {
                logger.warn("Access denied: User {} attempted to update another instructor's profile", username);
                throw new AccessDeniedException("You are not allowed to update another instructor's profile.");
            }
        }

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> {
                    logger.error("Profile with ID: {} not found", dto.getProfileId());
                    return new ResourceNotFoundException("Profile with ID " + dto.getProfileId() + " not found");
                });

        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> {
                    logger.error("Instructor with ID: {} not found", dto.getInstructorId());
                    return new ResourceNotFoundException("Instructor with ID " + dto.getInstructorId() + " not found");
                });

        instructorProfileMapper.updateEntity(entity, dto, profile, instructor);
        InstructorProfile updated = instructorProfileRepository.save(entity);
        logger.info("Successfully updated instructor profile with ID: {}", id);
        return ResponseEntity.ok(instructorProfileAssembler.toModel(instructorProfileMapper.toDTO(updated)));
    }

    @Override
    public ResponseEntity<?> deleteInstructorProfile(Long id) {
        logger.info("Deleting instructor profile with ID: {}", id);
        InstructorProfile entity = instructorProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("InstructorProfile with ID: {} not found", id);
                    return new ResourceNotFoundException("InstructorProfile with ID " + id + " not found");
                });
        instructorProfileRepository.delete(entity);
        logger.info("Successfully deleted instructor profile with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}