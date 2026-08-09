package com.example.project.Notifications.UserPrefrence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User Preferences", description = "Manage user notification preferences")
@RestController
@RequestMapping("/user-preferences")
public class UserPreferenceController {

    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceController.class);

    private final UserPreferenceService service;

    public UserPreferenceController(UserPreferenceService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a user preference")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User preference created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<EntityModel<UserPreferenceDTO>> createPreference(@Valid @RequestBody UserPreferenceDTO dto) {
        logger.info("Creating a new user preference for user ID: {}", dto.getUserId());
        ResponseEntity<EntityModel<UserPreferenceDTO>> response = service.createPreference(dto);
        logger.info("Successfully created user preference for user ID: {}", dto.getUserId());
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get preference by ID")
    @ApiResponse(responseCode = "200", description = "User preference retrieved")
    @GetMapping("/{id}")
    public EntityModel<UserPreferenceDTO> getPreferenceById(@PathVariable Long id) {
        logger.info("Fetching user preference with ID: {}", id);
        EntityModel<UserPreferenceDTO> preference = service.getPreferenceById(id);
        logger.info("Successfully fetched user preference with ID: {}", id);
        return preference;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get preference by User ID")
    @ApiResponse(responseCode = "200", description = "User preference retrieved")
    @GetMapping("/user/{userId}")
    public EntityModel<UserPreferenceDTO> getPreferenceByUserId(@PathVariable Long userId) {
        logger.info("Fetching user preference for user ID: {}", userId);
        EntityModel<UserPreferenceDTO> preference = service.getPreferenceByUserId(userId);
        logger.info("Successfully fetched user preference for user ID: {}", userId);
        return preference;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all preferences (paginated)")
    @ApiResponse(responseCode = "200", description = "User preferences retrieved")
    @GetMapping
    public PagedModel<EntityModel<UserPreferenceDTO>> getAllPreferences(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable) {
        logger.info("Fetching all user preferences with pageable: {}", pageable);
        PagedModel<EntityModel<UserPreferenceDTO>> preferences = service.getAllPreferences(pageable);
        logger.info("Successfully fetched all user preferences");
        return preferences;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all preferences (default paging)")
    @ApiResponse(responseCode = "200", description = "User preferences retrieved")
    @GetMapping("/default")
    public PagedModel<EntityModel<UserPreferenceDTO>> getAllPreferencesNoArgs() {
        logger.info("Fetching all user preferences with default paging");
        Pageable defaultPageable = PageRequest.of(0, 5, Sort.by("id").descending());
        PagedModel<EntityModel<UserPreferenceDTO>> preferences = service.getAllPreferences(defaultPageable);
        logger.info("Successfully fetched all user preferences with default paging");
        return preferences;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(summary = "Update a user preference")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User preference updated"),
        @ApiResponse(responseCode = "404", description = "User preference not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserPreferenceDTO>> updatePreference(@PathVariable Long id, @Valid @RequestBody UserPreferenceDTO dto) {
        logger.info("Updating user preference with ID: {}", id);
        ResponseEntity<EntityModel<UserPreferenceDTO>> response = service.updatePreference(id, dto);
        logger.info("Successfully updated user preference with ID: {}", id);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user preference")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User preference deleted"),
        @ApiResponse(responseCode = "404", description = "User preference not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePreference(@PathVariable Long id) {
        logger.info("Deleting user preference with ID: {}", id);
        ResponseEntity<?> response = service.deletePreference(id);
        logger.info("Successfully deleted user preference with ID: {}", id);
        return response;
    }
}