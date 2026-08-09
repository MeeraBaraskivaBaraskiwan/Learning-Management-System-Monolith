package com.example.project.Progress;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/progress")
@Tag(name = "Progress", description = "APIs for managing progress")
public class ProgressController {

    private static final Logger logger = LoggerFactory.getLogger(ProgressController.class);
    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping("/enrollment/{enrollmentId}")
    @Operation(summary = "Get progress by enrollment ID", description = "Retrieve progress records for a specific enrollment")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")    
    public CollectionModel<EntityModel<ProgressDTO>> getProgressByEnrollment(@PathVariable Long enrollmentId) {
        logger.info("Fetching progress for enrollment ID: {}", enrollmentId);
        return progressService.getProgressByEnrollment(enrollmentId);
    }

    @PostMapping
    @Operation(summary = "Add progress", description = "Add a new progress record")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<?> addProgress(@Valid @RequestBody ProgressDTO progressDTO) {
        logger.info("Adding new progress: {}", progressDTO);
        return progressService.addProgress(progressDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete progress", description = "Delete a progress record by ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProgress(@PathVariable Long id) {
        logger.info("Deleting progress with ID: {}", id);
        return progressService.deleteProgress(id);
    }


    @GetMapping
@Operation(summary = "Get all progress records", description = "Retrieve all progress records")
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public CollectionModel<EntityModel<ProgressDTO>> getAllProgress() {
    logger.info("Fetching all progress records");
    return progressService.getAllProgress();
}

}