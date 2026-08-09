package com.example.project.Assessments.AssessmentsAssignmentDetails;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Assignment Details", description = "Endpoints for managing assignment metadata")
@RestController
@RequestMapping("/assignments/details")
public class AssignmentDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentDetailsController.class);

    private final AssignmentDetailsService service;

    public AssignmentDetailsController(AssignmentDetailsService service) {
        this.service = service;
    }

    @Operation(summary = "Get all assignment details", description = "Returns paginated list of all assignment details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<AssignmentDetailsDTO>> getAll(
            @PageableDefault(size = 5) Pageable pageable
    ) {
        logger.info("Fetching all assignment details");
        return service.getAll(pageable);
    }

    @Operation(summary = "Get assignment detail by assessment ID", description = "Returns the assignment details for a specific assessment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment detail found"),
        @ApiResponse(responseCode = "404", description = "Assignment detail not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/assessment/{assessmentId}")
    public EntityModel<AssignmentDetailsDTO> getByAssessmentId(@PathVariable Long assessmentId) {
        logger.info("Fetching assignment detail for assessment ID: {}", assessmentId);
        return service.getByAssessmentId(assessmentId);
    }

    @Operation(summary = "Get assignment detail by ID", description = "Returns assignment details by its unique ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment detail found"),
        @ApiResponse(responseCode = "404", description = "Assignment detail not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/{id}")
    public EntityModel<AssignmentDetailsDTO> getById(@PathVariable Long id) {
        logger.info("Fetching assignment detail by ID: {}", id);
        return service.getById(id);
    }

    @Operation(summary = "Create assignment detail", description = "Creates new assignment detail metadata")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Assignment detail created"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AssignmentDetailsDTO dto) {
        logger.info("Creating assignment detail for assessment ID: {}", dto.getAssessmentId());
        return service.create(dto);
    }

    @Operation(summary = "Update assignment detail", description = "Updates existing assignment detail or creates if not exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment detail updated"),
        @ApiResponse(responseCode = "201", description = "Assignment detail created"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody AssignmentDetailsDTO dto) {
        logger.info("Updating assignment detail with ID: {}", id);
        return service.update(id, dto);
    }

    @Operation(summary = "Publish assignment detail", description = "Marks assignment as published")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment published"),
        @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PatchMapping("/{id}/publish")
    public ResponseEntity<?> publish(@PathVariable Long id) {
        logger.info("Publishing assignment detail with ID: {}", id);
        return service.publish(id);
    }

    @Operation(summary = "Delete assignment detail", description = "Deletes assignment metadata by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Assignment deleted"),
        @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        logger.info("Deleting assignment detail with ID: {}", id);
        return service.delete(id);
    }
}
