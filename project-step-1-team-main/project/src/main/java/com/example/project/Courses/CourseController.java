package com.example.project.Courses;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Tag(name = "Course Management", description = "Operations related to course management")   
@RestController
@RequestMapping("/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve all courses", description = "Returns a paginated list of all courses.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of courses")
    })
    @GetMapping
    public CollectionModel<EntityModel<CourseDTO>> all(Pageable pageable) {
        logger.info("Fetching all courses with pageable: {}", pageable);
        return courseService.all(pageable);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new course", description = "Creates a new course if a course with the same code does not exist.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Course already exists or invalid input")
    })
    @PostMapping
    public ResponseEntity<?> newCourse(@Valid @RequestBody CourseDTO newCourse) {
        logger.info("Creating new course with code: {}", newCourse.getCode());
        return courseService.newCourse(newCourse);
    }
    

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve a course by ID", description = "Fetches course details using the course ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public EntityModel<CourseDTO> one(@PathVariable Long id) {
        logger.info("Fetching course with ID: {}", id);
        return courseService.one(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a course", description = "Updates course details based on the provided ID and data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course updated successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@RequestBody CourseDTO updatedCourse, @PathVariable Long id) {
        logger.info("Updating course with ID: {}", id);
        return courseService.updateCourse(updatedCourse, id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a course", description = "Deletes a course using its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Course does not exist")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        logger.info("Deleting course with ID: {}", id);
        return courseService.deleteCourse(id);
    }
}
