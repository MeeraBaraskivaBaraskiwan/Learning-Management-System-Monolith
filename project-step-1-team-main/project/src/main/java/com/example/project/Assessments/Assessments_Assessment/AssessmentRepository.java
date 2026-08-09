package com.example.project.Assessments.Assessments_Assessment;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    Page<Assessment> findAll(Pageable pageable); 
    Page<Assessment> findByCourseCode(String courseCode, Pageable pageable); 
    Page<Assessment> findByCourse_Id(Long courseId, Pageable pageable);

}

