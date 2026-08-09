package com.example.project.CourseInstructors;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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


@Tag(name = "Course Instructor Management", description = "Operations related to assigning instructors to courses")
@RestController
@RequestMapping("/course-instructors")
public class CourseInstructorController {
    private static final Logger logger = LoggerFactory.getLogger(CourseInstructorController.class);


    private final CourseInstructorService courseInstructorService;

    public CourseInstructorController(CourseInstructorService courseInstructorService) {
        this.courseInstructorService = courseInstructorService;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve all course instructors", description = "Returns a paginated list of all course instructors.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course instructors")
    })
    @GetMapping
    public CollectionModel<EntityModel<CourseInstructorDTO>> all(Pageable pageable) {
        logger.info("Fetching all course instructors with pageable: {}", pageable);
        return courseInstructorService.all(pageable);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve a course instructor by ID", description = "Fetches course instructor details using the instructor assignment ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course instructor"),
        @ApiResponse(responseCode = "404", description = "Course instructor not found")
    })
    @GetMapping("/{id}")
    public EntityModel<CourseInstructorDTO> one(@PathVariable Long id) {
        logger.info("Fetching course instructor with id: {}", id);
        return courseInstructorService.one(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign an instructor to a course", description = "Creates an assignment of an instructor to a course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Instructor assigned to course successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input or duplicate assignment")
    })
    @PostMapping
    public ResponseEntity<?> assignInstructorToCourse(@Valid @RequestBody CourseInstructorDTO courseInstructorDTO) {
        logger.info("Assigning instructor {} to course {} for semester {} section {}",
        courseInstructorDTO.getInstructorId(),
        courseInstructorDTO.getCourseId(),
        courseInstructorDTO.getSectionId());
        return courseInstructorService.assignInstructorToCourse(courseInstructorDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove instructor from course", description = "Removes the instructor assignment from a course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instructor removed from course successfully"),
        @ApiResponse(responseCode = "404", description = "Course instructor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeInstructorFromCourse(@PathVariable Long id) {
        logger.info("Removing course instructor assignment with id: {}", id);
        return courseInstructorService.removeInstructorFromCourse(id);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get instructors by course", description = "Retrieves a list of instructors assigned to a specific course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved instructors for course")
    })
    @GetMapping("/course/{courseId}")
    public CollectionModel<EntityModel<CourseInstructorDTO>> getInstructorsByCourse(@PathVariable Long courseId) {
        logger.info("Fetching instructors for course id: {}", courseId);
        return courseInstructorService.getInstructorsByCourse(courseId);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get courses by instructor", description = "Retrieves a list of courses assigned to a specific instructor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved courses for instructor")
    })
    @GetMapping("/instructor/{instructorId}")
    public CollectionModel<EntityModel<CourseInstructorDTO>> getCoursesByInstructor(@PathVariable Long instructorId) {
        logger.info("Fetching courses for instructor id: {}", instructorId);
        return courseInstructorService.getCoursesByInstructor(instructorId);
    }
}
