package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import jakarta.validation.Valid;

import java.util.Optional;

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

@Tag(name = "Assignment Submissions", description = "Endpoints for managing assignment submissions")
@RestController
@RequestMapping("/assignments/submissions")
public class AssignmentSubmissionController {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentSubmissionController.class);

    private final AssignmentSubmissionService service;

    public AssignmentSubmissionController(AssignmentSubmissionService service) {
        this.service = service;
    }

    @Operation(summary = "Get all assignment submissions", description = "Returns paginated list of all assignment submissions")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<AssignmentSubmissionDTO>> getAllAssignmentSubmissions(
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching all assignment submissions");
        return service.getAllAssignmentSubmissions(pageable);
    }

    @Operation(summary = "Get submission by ID", description = "Fetches a submission by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission found"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<AssignmentSubmissionDTO> getAssignmentSubmissionById(@PathVariable Long id) {
        logger.info("Fetching assignment submission by ID: {}", id);
        return service.getAssignmentSubmissionById(id);
    }

    @Operation(summary = "Get submissions by assignment ID", description = "Fetches all submissions for a specific assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submissions found"),
        @ApiResponse(responseCode = "404", description = "Assignment not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/assignment/{assignmentId}")
    public PagedModel<EntityModel<AssignmentSubmissionDTO>> getSubmissionsByAssignmentId(
            @PathVariable Long assignmentId,
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching submissions by assignment ID: {}", assignmentId);
        return service.getSubmissionsByAssignmentId(assignmentId, pageable);
    }

    @Operation(summary = "Get submissions by student ID", description = "Fetches all submissions made by a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submissions found"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/student/{studentId}")
    public PagedModel<EntityModel<AssignmentSubmissionDTO>> getSubmissionsByStudentId(
            @PathVariable Long studentId,
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching submissions by student ID: {}", studentId);
        return service.getSubmissionsByStudentId(studentId, pageable);
    }

  @Operation(summary = "Get a submission by assignment and student",
           description = "Fetches a submission for a given student and assignment")
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "Submission found"),
  @ApiResponse(responseCode = "404", description = "Submission not found")
})
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
@GetMapping("/assignment/{assignmentId}/student/{studentId}")
public ResponseEntity<EntityModel<AssignmentSubmissionDTO>> getSubmissionByAssignmentIdAndStudentId(
    @PathVariable Long assignmentId,
    @PathVariable Long studentId
) {
  logger.info("Fetching submission for assignment ID: {}, student ID: {}", 
              assignmentId, studentId);

  // service returns Optional.empty() if not found
  Optional<AssignmentSubmissionDTO> optDto =
      service.findSubmissionByAssignmentAndStudent(assignmentId, studentId);

  if (optDto.isPresent()) {
    var model = EntityModel.of(optDto.get());
    return ResponseEntity.ok(model);
  } else {
    return ResponseEntity.notFound().build();
  }
}


    @Operation(summary = "Submit assignment", description = "Allows a student to submit an assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Submission created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> createAssignmentSubmission(@Valid @RequestBody AssignmentSubmissionDTO dto) {
        logger.info("Student {} submitting assignment ID: {}", dto.getStudentId(), dto.getAssignmentId());
        return service.createAssignmentSubmission(dto);
    }

    @Operation(summary = "Update assignment submission", description = "Allows a student to update their submission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission updated"),
        @ApiResponse(responseCode = "201", description = "Submission created"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignmentSubmission(@PathVariable Long id, @Valid @RequestBody AssignmentSubmissionDTO dto) {
        logger.info("Updating submission ID: {} by student {}", id, dto.getStudentId());
        return service.updateAssignmentSubmission(id, dto);
    }

    @Operation(summary = "Delete submission", description = "Allows instructor or admin to delete a submission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Submission deleted"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignmentSubmission(@PathVariable Long id) {
        logger.info("Deleting assignment submission ID: {}", id);
        return service.deleteAssignmentSubmission(id);
    }

    @Operation(summary = "Provide feedback", description = "Allows instructors to add or update feedback on a submission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback updated"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PatchMapping("/{submissionId}/feedback")
    public ResponseEntity<?> addOrUpdateFeedback(@PathVariable Long submissionId, @RequestParam String feedback) {
        logger.info("Instructor updating feedback for submission ID: {}", submissionId);
        return service.addOrUpdateFeedback(submissionId, feedback);
    }
}
