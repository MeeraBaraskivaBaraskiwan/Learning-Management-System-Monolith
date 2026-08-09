package com.example.project.Profiles.InstructorProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Instructor Profiles", description = "Operations related to instructor profiles")
@RestController
@RequestMapping("/instructor-profiles")
public class InstructorProfileController {

    private static final Logger logger = LoggerFactory.getLogger(InstructorProfileController.class);

    private final InstructorProfileService instructorProfileService;

    public InstructorProfileController(InstructorProfileService instructorProfileService) {
        this.instructorProfileService = instructorProfileService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create an instructor profile")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Instructor profile created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<EntityModel<InstructorProfileDTO>> createInstructorProfile(@RequestBody InstructorProfileDTO dto) {
        logger.info("Creating a new instructor profile for instructorId: {}", dto.getInstructorId());
        ResponseEntity<EntityModel<InstructorProfileDTO>> response = instructorProfileService.createInstructorProfile(dto);
        logger.info("Successfully created instructor profile for instructorId: {}", dto.getInstructorId());
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get instructor profile by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Instructor profile found"),
        @ApiResponse(responseCode = "404", description = "Instructor profile not found")
    })
    @GetMapping("/{id}")
    public EntityModel<InstructorProfileDTO> getInstructorProfileById(@PathVariable Long id) {
        logger.info("Fetching instructor profile with ID: {}", id);
        EntityModel<InstructorProfileDTO> profile = instructorProfileService.getInstructorProfileById(id);
        logger.info("Successfully fetched instructor profile with ID: {}", id);
        return profile;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get instructor profile by instructor ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Instructor profile found"),
        @ApiResponse(responseCode = "404", description = "Instructor profile not found")
    })
    @GetMapping("/instructor/{instructorId}")
    public EntityModel<InstructorProfileDTO> getInstructorProfileByInstructorId(@PathVariable Long instructorId) {
        logger.info("Fetching instructor profile for instructorId: {}", instructorId);
        EntityModel<InstructorProfileDTO> profile = instructorProfileService.getInstructorProfileByInstructorId(instructorId);
        logger.info("Successfully fetched instructor profile for instructorId: {}", instructorId);
        return profile;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all instructor profiles")
    @ApiResponse(responseCode = "200", description = "Instructor profiles retrieved")
    @GetMapping
    public PagedModel<EntityModel<InstructorProfileDTO>> getAllInstructorProfiles(@PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching all instructor profiles with pageable: {}", pageable);
        PagedModel<EntityModel<InstructorProfileDTO>> profiles = instructorProfileService.getAllInstructorProfiles(pageable);
        logger.info("Successfully fetched all instructor profiles");
        return profiles;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Update instructor profile")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Instructor profile updated"),
        @ApiResponse(responseCode = "404", description = "Instructor profile not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<InstructorProfileDTO>> updateInstructorProfile(
            @PathVariable Long id,
            @RequestBody InstructorProfileDTO dto,
            Authentication authentication) {
        logger.info("Updating instructor profile with ID: {}", id);
        ResponseEntity<EntityModel<InstructorProfileDTO>> response = instructorProfileService.updateInstructorProfile(id, dto, authentication);
        logger.info("Successfully updated instructor profile with ID: {}", id);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete instructor profile")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Instructor profile deleted"),
        @ApiResponse(responseCode = "404", description = "Instructor profile not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInstructorProfile(@PathVariable Long id) {
        logger.info("Deleting instructor profile with ID: {}", id);
        ResponseEntity<?> response = instructorProfileService.deleteInstructorProfile(id);
        logger.info("Successfully deleted instructor profile with ID: {}", id);
        return response;
    }
}