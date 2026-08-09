package com.example.project.Students;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Tag(name = "Students", description = "Student management APIs")
@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {
    
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

     public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

 @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all students")
    @ApiResponse(responseCode = "200", description = "Students retrieved successfully")
    @GetMapping
    public CollectionModel<EntityModel<StudentDTO>> all(Pageable pageable) {
        logger.info("Fetching all students with pageable: {}", pageable);
        CollectionModel<EntityModel<StudentDTO>> studentsPage = studentService.all(pageable);
        logger.info("Successfully fetched students");
        return studentsPage;
    }



    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new student")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<?> newStudent(@Valid @RequestBody StudentDTO newStudent) {
        logger.info("Creating a new student: {}", newStudent);
        ResponseEntity<?> response = studentService.newStudent(newStudent);
        logger.info("Student created successfully");
        return response;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get student by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public EntityModel<StudentDTO> one(@PathVariable Long id) {
        logger.info("Fetching student with ID: {}", id);
        EntityModel<StudentDTO> student = studentService.one(id);
        logger.info("Successfully fetched student with ID: {}", id);
        return student;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(summary = "Update a student")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@Valid @RequestBody StudentDTO newStudent, @PathVariable Long id) {
        logger.info("Updating student with ID: {}", id);
        ResponseEntity<?> response = studentService.updateStudent(newStudent, id);
        logger.info("Successfully updated student with ID: {}", id);
        return response;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a student")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student deleted"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        logger.info("Deleting student with ID: {}", id);
        ResponseEntity<?> response = studentService.deleteStudent(id);
        logger.info("Successfully deleted student with ID: {}", id);
        return response;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Find student by student ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/studentId/{studentId}")
    public EntityModel<StudentDTO> findByStudentId(@PathVariable String studentId) {
        logger.info("Fetching student with student ID: {}", studentId);
        EntityModel<StudentDTO> student = studentService.findByStudentId(studentId);
        logger.info("Successfully fetched student with student ID: {}", studentId);
        return student;
    }



    // in StudentController.java
@PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
@Operation(summary = "Get the current user's student record")
@GetMapping("/by-user/{userId}")
public EntityModel<StudentDTO> byUser(@PathVariable Long userId) {
    logger.info("GET /students/by-user/{}", userId);
    return studentService.findByUserId(userId);
}


 @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
   @Operation(summary = "Get all students (plain JSON)")
  @GetMapping("/simple")
   public List<StudentDTO> simpleAll() {
     return studentService.simpleAll();
   }


    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
  @GetMapping("/simple/instructor/{instrId}")
  public List<StudentDTO> simpleEnrolled(@PathVariable Long instrId) {
    return studentService.simpleEnrolledByInstructor(instrId);
  }

}
