package com.example.project.Assessments.AssessmentsAssessmentGrade;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface AssessmentGradeService {

    PagedModel<EntityModel<AssessmentGradeDTO>> getAllGrades(Pageable pageable);

    EntityModel<AssessmentGradeDTO> getGradeById(Long id);

    PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByAssessmentId(Long assessmentId, Pageable pageable);

    PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByStudentId(Long studentId, Pageable pageable);

    EntityModel<AssessmentGradeDTO> getGradeByAssessmentAndStudent(Long assessmentId, Long studentId);

PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByStudentAndCourse(String courseCode, Long studentId, Pageable pageable);

PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByCourseAndSection(String courseCode, int sectionNumber, Pageable pageable);


    ResponseEntity<?> createGrade(AssessmentGradeDTO dto);

    ResponseEntity<?> updateGrade(Long id, AssessmentGradeDTO dto);

    ResponseEntity<?> deleteGrade(Long id);
}
