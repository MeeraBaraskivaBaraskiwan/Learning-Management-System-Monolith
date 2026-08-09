package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface AssignmentSubmissionService {

    PagedModel<EntityModel<AssignmentSubmissionDTO>> getAllAssignmentSubmissions(Pageable pageable);

    EntityModel<AssignmentSubmissionDTO> getAssignmentSubmissionById(Long id);

    PagedModel<EntityModel<AssignmentSubmissionDTO>> getSubmissionsByAssignmentId(Long assignmentId, Pageable pageable);

    PagedModel<EntityModel<AssignmentSubmissionDTO>> getSubmissionsByStudentId(Long studentId, Pageable pageable);

    EntityModel<AssignmentSubmissionDTO> getSubmissionByAssignmentIdAndStudentId(Long assignmentId, Long studentId);

    ResponseEntity<?> createAssignmentSubmission(AssignmentSubmissionDTO dto);

    ResponseEntity<?> updateAssignmentSubmission(Long id, AssignmentSubmissionDTO dto);

    ResponseEntity<?> deleteAssignmentSubmission(Long id);

    ResponseEntity<?> addOrUpdateFeedback(Long submissionId, String feedback);

    Optional<AssignmentSubmissionDTO> findSubmissionByAssignmentAndStudent(
    Long assignmentId,
    Long studentId
);
}
