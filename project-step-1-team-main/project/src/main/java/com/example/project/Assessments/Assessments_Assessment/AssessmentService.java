package com.example.project.Assessments.Assessments_Assessment;


import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Pageable;

public interface AssessmentService {

    PagedModel<EntityModel<AssessmentDTO>> getAllAssessments(Pageable pageable);
    EntityModel<AssessmentDTO> getAssessmentById(Long id);
    PagedModel<EntityModel<AssessmentDTO>> getAssessmentsByCourseCode(String courseCode, Pageable pageable);
    ResponseEntity<?> createAssessment(AssessmentDTO dto);
    ResponseEntity<?> updateAssessment(Long id, AssessmentDTO dto);
    ResponseEntity<?> deleteAssessment(Long id);
    PagedModel<EntityModel<AssessmentDTO>> getAssessmentsByCourseId(Long courseId, Pageable pageable);

}

