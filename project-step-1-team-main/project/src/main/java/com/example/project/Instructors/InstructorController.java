package com.example.project.Instructors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructors")
@Tag(name = "Instructors", description = "API for managing instructors")
public class InstructorController {

    private static final Logger logger = LoggerFactory.getLogger(InstructorController.class);

    @Autowired
    private InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @Operation(summary = "Get all instructors", description = "Retrieve a paginated list of all instructors")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @GetMapping
    public CollectionModel<EntityModel<InstructorDTO>> all(Pageable pageable) {
        logger.info("Fetching all instructors");
        CollectionModel<EntityModel<InstructorDTO>> instructorsPage = instructorService.all(pageable);
        return instructorsPage;
    }

    @Operation(summary = "Create a new instructor", description = "Add a new instructor to the system")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> newInstructor(@Valid @RequestBody InstructorDTO newInstructor) {
        logger.info("Creating a new instructor");
        try {
            return instructorService.newInstructor(newInstructor);
        } catch (Exception e) {
            logger.error("Error creating instructor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating instructor: " + e.getMessage());
        }
    }

    @Operation(summary = "Get instructor by ID", description = "Retrieve details of a specific instructor by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')") 
    @GetMapping("/{id}")
    public EntityModel<InstructorDTO> one(@PathVariable Long id) {
        logger.info("Fetching instructor with ID: {}", id);
        return instructorService.one(id);
    }

    @Operation(summary = "Update an instructor", description = "Update details of an existing instructor")
    @PreAuthorize("hasAnyRole('ADMIN')")  
    @PutMapping("/{id}")
    public ResponseEntity<?> updateInstructor(@RequestBody InstructorDTO updatedInstructor, @PathVariable Long id) {
        logger.info("Updating instructor with ID: {}", id);
        return instructorService.updateInstructor(updatedInstructor, id);
    }

    @Operation(summary = "Delete an instructor", description = "Remove an instructor from the system")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInstructor(@PathVariable Long id) {
        logger.info("Deleting instructor with ID: {}", id);
        return instructorService.deleteInstructor(id);
    }


    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
@GetMapping("/by-user/{userId}")
public EntityModel<InstructorDTO> byUser(@PathVariable Long userId) {
  return instructorService.findByUserId(userId);
}

    


}
