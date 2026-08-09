package com.example.project.Profiles.StudentProfile;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

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
import jakarta.validation.Valid;

@Tag(name = "Student Profiles", description = "Operations related to student profiles")
@RestController
@RequestMapping("/student-profiles")
public class StudentProfileController {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileController.class);

    private final StudentProfileService studentProfileService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
private final StudentProfileRepository studentProfileRepository;
     public StudentProfileController(
            StudentProfileService studentProfileService,
            UserRepository userRepository,
            StudentRepository studentRepository,
            StudentProfileRepository studentProfileRepository
    ) {
        this.studentProfileService    = studentProfileService;
        this.userRepository           = userRepository;
        this.studentRepository        = studentRepository;
        this.studentProfileRepository = studentProfileRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a student profile")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student profile created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<EntityModel<StudentProfileDTO>> createStudentProfile(@Valid @RequestBody StudentProfileDTO dto) {
        logger.info("Creating a new student profile for studentId: {}", dto.getStudentId());
        ResponseEntity<EntityModel<StudentProfileDTO>> response = studentProfileService.createStudentProfile(dto);
        logger.info("Successfully created student profile for studentId: {}", dto.getStudentId());
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get student profile by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student profile found"),
        @ApiResponse(responseCode = "404", description = "Student profile not found")
    })
    @GetMapping("/{id}")
    public EntityModel<StudentProfileDTO> getStudentProfileById(@PathVariable Long id) {
        logger.info("Fetching student profile with ID: {}", id);
        EntityModel<StudentProfileDTO> profile = studentProfileService.getStudentProfileById(id);
        logger.info("Successfully fetched student profile with ID: {}", id);
        return profile;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get student profile by student ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student profile found"),
        @ApiResponse(responseCode = "404", description = "Student profile not found")
    })
    @GetMapping("/student/{studentId}")
    public EntityModel<StudentProfileDTO> getStudentProfileByStudentId(@PathVariable Long studentId) {
        logger.info("Fetching student profile for studentId: {}", studentId);
        EntityModel<StudentProfileDTO> profile = studentProfileService.getStudentProfileByStudentId(studentId);
        logger.info("Successfully fetched student profile for studentId: {}", studentId);
        return profile;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all student profiles")
    @ApiResponse(responseCode = "200", description = "Student profiles retrieved")
    @GetMapping
    public PagedModel<EntityModel<StudentProfileDTO>> getAllStudentProfiles(@PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching all student profiles with pageable: {}", pageable);
        PagedModel<EntityModel<StudentProfileDTO>> profiles = studentProfileService.getAllStudentProfiles(pageable);
        logger.info("Successfully fetched all student profiles");
        return profiles;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Update student profile")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student profile updated"),
        @ApiResponse(responseCode = "404", description = "Student profile not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<StudentProfileDTO>> updateStudentProfile(@PathVariable Long id, @Valid @RequestBody StudentProfileDTO dto) {
        logger.info("Updating student profile with ID: {}", id);
        ResponseEntity<EntityModel<StudentProfileDTO>> response = studentProfileService.updateStudentProfile(id, dto);
        logger.info("Successfully updated student profile with ID: {}", id);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete student profile")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Student profile deleted"),
        @ApiResponse(responseCode = "404", description = "Student profile not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentProfile(@PathVariable Long id) {
        logger.info("Deleting student profile with ID: {}", id);
        ResponseEntity<?> response = studentProfileService.deleteStudentProfile(id);
        logger.info("Successfully deleted student profile with ID: {}", id);
        return response;
    }

        // --- NEW: get “my” profile ---
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/me")
    public EntityModel<StudentProfileDTO> getMyProfile(Authentication auth) {
        // 1) find User by email (principal name)
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        // 2) find Student by User
        Student student = studentRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Not a student: " + email));
        // 3) delegate to your existing service
        return studentProfileService.getStudentProfileByStudentId(student.getId());
    }

    // --- NEW: update “my” profile ---
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/me")
    public ResponseEntity<EntityModel<StudentProfileDTO>> updateMyProfile(
        @Valid @RequestBody StudentProfileDTO dto,
        Authentication auth
    ) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        Student student = studentRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Not a student: " + email));
        // find the existing StudentProfile row
        Long profileId = studentProfileRepository
            .findByStudentId(student.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Profile not found"))
            .getId();
        // reuse your service
        return studentProfileService.updateStudentProfile(profileId, dto);
    }
}