package com.example.project.Profiles.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

@Tag(name = "Profiles", description = "Operations related to user profiles")
@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @Operation(summary = "Create a new profile")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Profile created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<EntityModel<ProfileDTO>> createProfile(@Valid @RequestBody ProfileDTO dto, @RequestParam Long userId) {
        logger.info("Creating a new profile for userId: {}", userId);
        ResponseEntity<EntityModel<ProfileDTO>> response = profileService.createProfile(dto, userId);
        logger.info("Successfully created a new profile for userId: {}", userId);
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @Operation(summary = "Get a profile by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile found"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{id}")
    public EntityModel<ProfileDTO> getProfileById(@PathVariable Long id) {
        logger.info("Fetching profile with ID: {}", id);
        EntityModel<ProfileDTO> profile = profileService.getProfileById(id);
        logger.info("Successfully fetched profile with ID: {}", id);
        return profile;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @Operation(summary = "Get profile by User ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile found"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/user/{userId}")
    public EntityModel<ProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        logger.info("Fetching profile for userId: {}", userId);
        EntityModel<ProfileDTO> profile = profileService.getProfileByUserId(userId);
        logger.info("Successfully fetched profile for userId: {}", userId);
        return profile;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all profiles")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully")
    })
    @GetMapping
    public PagedModel<EntityModel<ProfileDTO>> getAllProfiles(@PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching all profiles with pageable: {}", pageable);
        PagedModel<EntityModel<ProfileDTO>> profiles = profileService.getAllProfiles(pageable);
        logger.info("Successfully fetched all profiles");
        return profiles;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    @Operation(summary = "Update a profile by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProfileDTO>> updateProfile(@PathVariable Long id, @RequestBody ProfileDTO dto, @RequestParam Long userId) {
        logger.info("Updating profile with ID: {} for userId: {}", id, userId);
        ResponseEntity<EntityModel<ProfileDTO>> response = profileService.updateProfile(id, dto, userId);
        logger.info("Successfully updated profile with ID: {}", id);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a profile by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        logger.info("Deleting profile with ID: {}", id);
        ResponseEntity<?> response = profileService.deleteProfile(id);
        logger.info("Successfully deleted profile with ID: {}", id);
        return response;
    }
}