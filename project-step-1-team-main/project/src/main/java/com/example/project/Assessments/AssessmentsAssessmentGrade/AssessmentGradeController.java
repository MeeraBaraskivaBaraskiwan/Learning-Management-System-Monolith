package com.example.project.Assessments.AssessmentsAssessmentGrade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Assessment Grades", description = "Endpoints for managing assessment grades")
@RestController
@RequestMapping("/grades")
public class AssessmentGradeController {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentGradeController.class);

    private final AssessmentGradeService gradeService;

    public AssessmentGradeController(AssessmentGradeService gradeService) {
        this.gradeService = gradeService;
    }

    @Operation(summary = "Get all assessment grades", description = "Returns a paginated list of all assessment grades")
    @ApiResponse(responseCode = "200", description = "Grades retrieved successfully")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<AssessmentGradeDTO>> getAllGrades(
            @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        logger.info("Fetching all assessment grades with pageable: {}", pageable);
        return gradeService.getAllGrades(pageable);
    }

    @Operation(summary = "Get grade by ID", description = "Fetches a grade by its unique ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grade found"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<AssessmentGradeDTO> getGradeById(@PathVariable Long id) {
        logger.info("Fetching grade by ID: {}", id);
        return gradeService.getGradeById(id);
    }

    @Operation(summary = "Get grades by assessment ID", description = "Fetches all grades for a specific assessment")
    @ApiResponse(responseCode = "200", description = "Grades found")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/assessment/{assessmentId}")
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByAssessmentId(
            @PathVariable Long assessmentId,
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching grades for assessment ID: {}", assessmentId);
        return gradeService.getGradesByAssessmentId(assessmentId, pageable);
    }

    @Operation(summary = "Get grades by student ID", description = "Fetches all grades for a specific student")
    @ApiResponse(responseCode = "200", description = "Grades found")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/student/{studentId}")
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByStudentId(
            @PathVariable Long studentId,
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching grades for student ID: {}", studentId);
        return gradeService.getGradesByStudentId(studentId, pageable);
    }

    @Operation(summary = "Get grade by assessment and student", description = "Fetches a grade for a specific assessment and student")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grade found"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/assessment/{assessmentId}/student/{studentId}")
    public EntityModel<AssessmentGradeDTO> getGradeByAssessmentAndStudent(
            @PathVariable Long assessmentId,
            @PathVariable Long studentId) {
        logger.info("Fetching grade for assessment ID: {} and student ID: {}", assessmentId, studentId);
        return gradeService.getGradeByAssessmentAndStudent(assessmentId, studentId);
    }

    @Operation(summary = "Get grades by student and course", description = "Fetches all grades for a specific student and course")
    @ApiResponse(responseCode = "200", description = "Grades found")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/student/{studentId}/course/{courseCode}")
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByStudentAndCourse(
            @PathVariable String courseCode,
            @PathVariable Long studentId,
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching grades for student ID: {} and course code: {}", studentId, courseCode);
        return gradeService.getGradesByStudentAndCourse(courseCode, studentId, pageable);
    }

    @Operation(summary = "Get grades by course and section", description = "Fetches all grades for a specific course and section")
    @ApiResponse(responseCode = "200", description = "Grades found")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/course/{courseCode}/section/{sectionNumber}")
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByCourseAndSection(
            @PathVariable String courseCode,
            @PathVariable int sectionNumber,
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching grades for course code: {} and section number: {}", courseCode, sectionNumber);
        return gradeService.getGradesByCourseAndSection(courseCode, sectionNumber, pageable);
    }

    @Operation(summary = "Create a new grade", description = "Creates a new grade for an assessment")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Grade created"),
        @ApiResponse(responseCode = "404", description = "Assessment, student, or submission not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createGrade(@Valid @RequestBody AssessmentGradeDTO dto) {
        logger.info("Creating grade for assessment ID: {} and student ID: {}", dto.getAssessmentId(), dto.getStudentId());
        return gradeService.createGrade(dto);
    }

    @Operation(summary = "Update a grade", description = "Updates an existing grade or creates a new one if it doesn't exist")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grade updated"),
        @ApiResponse(responseCode = "201", description = "Grade created"),
        @ApiResponse(responseCode = "404", description = "Grade or related entities not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentGradeDTO dto) {
        logger.info("Updating grade with ID: {}", id);
        return gradeService.updateGrade(id, dto);
    }

    @Operation(summary = "Delete a grade", description = "Deletes a grade by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Grade deleted"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGrade(@PathVariable Long id) {
        logger.info("Deleting grade with ID: {}", id);
        return gradeService.deleteGrade(id);
    }
}
