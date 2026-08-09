package com.example.project.Profiles.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ProfileAssembler profileAssembler;
    private final PagedResourcesAssembler<ProfileDTO> pagedAssembler;
    private final UserRepository userRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository, 
                              ProfileMapper profileMapper, 
                              ProfileAssembler profileAssembler,
                              PagedResourcesAssembler<ProfileDTO> pagedAssembler,
                              UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
        this.profileAssembler = profileAssembler;
        this.pagedAssembler = pagedAssembler;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<EntityModel<ProfileDTO>> createProfile(ProfileDTO dto, Long userId) {
        logger.info("Creating a new profile for userId: {}", userId);
        User user = userRepository.findById(userId)
          .orElseThrow(() -> {
              logger.error("User with ID: {} not found", userId);
              return new ResourceNotFoundException("User with ID " + userId + " not found");
          });
        Profile profile = profileMapper.toEntity(dto, user);
        Profile saved = profileRepository.save(profile);
        logger.info("Successfully created a new profile for userId: {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(profileAssembler.toModel(profileMapper.toDTO(saved)));
    }

    @Override
    public EntityModel<ProfileDTO> getProfileById(Long id) {
        logger.info("Fetching profile with ID: {}", id);
        Profile profile = profileRepository.findById(id)
        .orElseThrow(() -> {
            logger.error("Profile with ID: {} not found", id);
            return new ResourceNotFoundException("Profile with ID " + id + " not found");
        });
        logger.info("Successfully fetched profile with ID: {}", id);
        return profileAssembler.toModel(profileMapper.toDTO(profile));
    }

    @Override
    public EntityModel<ProfileDTO> getProfileByUserId(Long userId) {
        logger.info("Fetching profile for userId: {}", userId);
        Profile profile = profileRepository.findByUser_Id(userId)
        .orElseThrow(() -> {
            logger.error("Profile not found for userId: {}", userId);
            return new ResourceNotFoundException("Profile not found for user ID " + userId);
        });
        logger.info("Successfully fetched profile for userId: {}", userId);
        return profileAssembler.toModel(profileMapper.toDTO(profile));
    }

    @Override
    public PagedModel<EntityModel<ProfileDTO>> getAllProfiles(Pageable pageable) {
        logger.info("Fetching all profiles with pageable: {}", pageable);
        PagedModel<EntityModel<ProfileDTO>> profiles = pagedAssembler.toModel(
            profileRepository.findAll(pageable).map(profileMapper::toDTO), profileAssembler);
        logger.info("Successfully fetched all profiles");
        return profiles;
    }

    @Override
    public ResponseEntity<EntityModel<ProfileDTO>> updateProfile(Long id, ProfileDTO dto, Long userId) {
        logger.info("Updating profile with ID: {} for userId: {}", id, userId);
        Profile profile = profileRepository.findById(id)
        .orElseThrow(() -> {
            logger.error("Profile with ID: {} not found", id);
            return new ResourceNotFoundException("Profile with ID " + id + " not found");
        });
        User user = userRepository.findById(userId)
        .orElseThrow(() -> {
            logger.error("User with ID: {} not found", userId);
            return new ResourceNotFoundException("User with ID " + userId + " not found");
        });
        profileMapper.updateEntity(profile, dto);
        Profile updated = profileRepository.save(profile);
        logger.info("Successfully updated profile with ID: {}", id);
        return ResponseEntity.ok(profileAssembler.toModel(profileMapper.toDTO(updated)));
    }

    @Override
    public ResponseEntity<?> deleteProfile(Long id) {
        logger.info("Deleting profile with ID: {}", id);
        if (!profileRepository.existsById(id)) {
            logger.error("Profile with ID: {} not found", id);
            throw new ResourceNotFoundException("Profile with ID " + id + " not found");
        }
        profileRepository.deleteById(id);
        logger.info("Successfully deleted profile with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}