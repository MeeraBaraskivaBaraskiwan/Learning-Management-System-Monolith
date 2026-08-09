package com.example.project.Enrollments;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;
import com.example.project.Users.UserRepository;
import com.example.project.Sections.SectionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import com.example.project.Users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Tag(name = "Enrollment Management", description = "Operations related to student enrollments")
@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    private final EnrollmentService enrollmentService;
    private final UserRepository    userRepository;       
    private final StudentRepository studentRepository; 
     private final SectionRepository sectionRepository;   

   public EnrollmentController(EnrollmentService enrollmentService,
                                UserRepository userRepository,
                                StudentRepository studentRepository,
                                SectionRepository sectionRepository) {
        this.enrollmentService  = enrollmentService;
        this.userRepository     = userRepository;
        this.studentRepository  = studentRepository;
        this.sectionRepository   = sectionRepository;
    }



     @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve all enrollments", description = "Returns a paginated list of all enrollments.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments")
    })
    @GetMapping
    public CollectionModel<EntityModel<EnrollmentDTO>> all(Pageable pageable) {
        logger.info("Fetching all enrollments with pageable: {}", pageable);
        return enrollmentService.all(pageable);
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve enrollment by ID", description = "Fetches enrollment details using the enrollment ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollment"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @GetMapping("/{id}")
    public EntityModel<EnrollmentDTO> one(@PathVariable Long id) {
        logger.info("Fetching enrollment with ID: {}", id);
        return enrollmentService.one(id);
    }


     @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
     @Operation(summary = "Enroll student", description = "Enrolls a student in a course.")
     @ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "Student enrolled successfully"),
         @ApiResponse(responseCode = "400", description = "Bad Request - Enrollment already exists or invalid input")
     })
    @PostMapping
    public ResponseEntity<?> enrollStudent(@Valid @RequestBody EnrollmentDTO dto) {
        logger.info("Enrolling student {} into section {}",
            dto.getStudentId(), dto.getSectionId());
        return enrollmentService.enrollStudent(dto);
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Update enrollment progress", description = "Updates enrollment progress based on course content completion.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollment progress updated successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Module already completed or invalid input")
    })
    @PutMapping("/{id}/progress")
    public ResponseEntity<?> updateEnrollmentProgress(@PathVariable Long id, @RequestParam Long courseContentId) {
        logger.info("Updating enrollment progress for enrollment ID: {} with courseContent ID: {}", id, courseContentId);
        return enrollmentService.updateEnrollmentProgress(id, courseContentId);
    }



    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
    @Operation(summary = "Remove enrollment", description = "Deletes an enrollment by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Enrollment deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeEnrollment(@PathVariable Long id) {
        logger.info("Removing enrollment with ID: {}", id);
        return enrollmentService.removeEnrollment(id);
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get enrollments by student", description = "Retrieves enrollments for a specific student.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments for student")
    })
    @GetMapping("/student/{studentId}")
    public CollectionModel<EntityModel<EnrollmentDTO>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        logger.info("Fetching enrollments for student with ID: {}", studentId);
        return enrollmentService.getEnrollmentsByStudent(studentId);
    }


@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
@GetMapping("/by-user-simple/{userId}")
public List<EnrollmentDTO> getEnrollmentsByUserSimple(@PathVariable Long userId) {
  User user = userRepository.findById(userId)
    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
  Student student = studentRepository.findByUser(user)
    .orElseThrow(() -> new ResourceNotFoundException("No student record for user " + userId));
  return enrollmentService.getPlainEnrollmentsByStudent(student.getId());
}



    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get enrollments by course", description = "Retrieves enrollments for a specific course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved enrollments for course")
    })
    @GetMapping("/course/{courseId}")
    public CollectionModel<EntityModel<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        logger.info("Fetching enrollments for course with ID: {}", courseId);
        return enrollmentService.getEnrollmentsByCourse(courseId);
    }
}
