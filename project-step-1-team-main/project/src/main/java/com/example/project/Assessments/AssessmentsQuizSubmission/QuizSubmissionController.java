package com.example.project.Assessments.AssessmentsQuizSubmission;

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
import jakarta.validation.Valid;

@Tag(name = "Quiz Submissions", description = "Endpoints for managing quiz submissions")
@RestController
@RequestMapping("/quiz-submissions")
public class QuizSubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(QuizSubmissionController.class);

    private final QuizSubmissionService quizSubmissionService;

    public QuizSubmissionController(QuizSubmissionService quizSubmissionService) {
        this.quizSubmissionService = quizSubmissionService;
    }

    @Operation(summary = "Get all quiz submissions", description = "Fetches all quiz submissions (admin/instructor only).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<QuizSubmissionDTO>> getAllQuizSubmissions(
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching all quiz submissions");
        return quizSubmissionService.getAllQuizSubmissions(pageable);
    }

    @Operation(summary = "Get quiz submission by ID", description = "Fetches a single quiz submission by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission found"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<QuizSubmissionDTO> getQuizSubmissionById(@PathVariable Long id) {
        logger.info("Fetching quiz submission by ID: {}", id);
        return quizSubmissionService.getQuizSubmissionById(id);
    }

    @Operation(summary = "Get submissions by quiz ID", description = "Fetches all submissions for a specific quiz.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submissions found"),
        @ApiResponse(responseCode = "404", description = "No submissions found for quiz")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/quiz/{quizId}")
    public PagedModel<EntityModel<QuizSubmissionDTO>> getQuizSubmissionsByQuizId(
            @PathVariable Long quizId, @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching submissions for quiz ID: {}", quizId);
        return quizSubmissionService.getQuizSubmissionsByQuizId(quizId, pageable);
    }

    @Operation(summary = "Get submissions by student ID", description = "Fetches all submissions made by a student (admin/instructor only).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submissions found"),
        @ApiResponse(responseCode = "404", description = "No submissions found for student")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/student/{studentId}")
    public PagedModel<EntityModel<QuizSubmissionDTO>> getQuizSubmissionsByStudentId(
            @PathVariable Long studentId, @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching submissions by student ID: {}", studentId);
        return quizSubmissionService.getQuizSubmissionsByStudentId(studentId, pageable);
    }

    @Operation(summary = "Create quiz submission", description = "Allows a student to submit a quiz. Only one submission allowed per quiz.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Submission created"),
        @ApiResponse(responseCode = "400", description = "Submission already exists or data invalid")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> createQuizSubmission(@Valid @RequestBody QuizSubmissionDTO dto) {
        logger.info("Student {} is submitting quiz ID: {}", dto.getStudentId(), dto.getQuizId());
        return quizSubmissionService.createQuizSubmission(dto);
    }

    @Operation(summary = "Update quiz submission", description = "Allows a student to update their quiz submission before deadline or locking.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission updated"),
        @ApiResponse(responseCode = "201", description = "Submission created"),
        @ApiResponse(responseCode = "404", description = "Submission or quiz/student not found")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuizSubmission(@PathVariable Long id, @Valid @RequestBody QuizSubmissionDTO dto) {
        logger.info("Updating quiz submission ID: {} for student {}", id, dto.getStudentId());
        return quizSubmissionService.updateQuizSubmission(id, dto);
    }

    @Operation(summary = "Delete quiz submission", description = "Allows admin or instructor to delete a quiz submission.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Submission deleted"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuizSubmission(@PathVariable Long id) {
        logger.info("Deleting quiz submission with ID: {}", id);
        return quizSubmissionService.deleteQuizSubmission(id);
    }
}
