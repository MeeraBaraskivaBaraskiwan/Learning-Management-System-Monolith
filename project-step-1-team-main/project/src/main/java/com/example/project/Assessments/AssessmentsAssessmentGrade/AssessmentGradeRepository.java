package com.example.project.Assessments.AssessmentsAssessmentGrade;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentGradeRepository extends JpaRepository<AssessmentGrade, Long> {

    Optional<AssessmentGrade> findByAssessmentIdAndStudentId(Long assessmentId, Long studentId);

    Page<AssessmentGrade> findByAssessmentId(Long assessmentId, Pageable pageable);

    Page<AssessmentGrade> findByStudentId(Long studentId, Pageable pageable);

    Page<AssessmentGrade> findByStudentIdAndAssessment_Course_Code(Long studentId, String courseCode, Pageable pageable);

Page<AssessmentGrade> findByAssessment_Course_CodeAndAssessment_SectionNumber(String courseCode, int sectionNumber, Pageable pageable);

 List<AssessmentGrade> findTop10ByAssessment_InstructorIdOrderByCreatedAtDesc(Long instructorId);
}