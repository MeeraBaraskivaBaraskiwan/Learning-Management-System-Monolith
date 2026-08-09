package com.example.project.Assessments.AssessmentsAssignmentDetails;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface AssignmentDetailsService {
    PagedModel<EntityModel<AssignmentDetailsDTO>> getAll(Pageable pageable);
    EntityModel<AssignmentDetailsDTO> getById(Long id);
    EntityModel<AssignmentDetailsDTO> getByAssessmentId(Long assessmentId);
    ResponseEntity<?> create(AssignmentDetailsDTO dto);
    ResponseEntity<?> update(Long id, AssignmentDetailsDTO dto);
    ResponseEntity<?> publish(Long id);
    ResponseEntity<?> delete(Long id);
}