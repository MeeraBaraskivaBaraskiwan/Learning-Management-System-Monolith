package com.example.project.Assessments.AssessmentsAssignmentDetails;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AssignmentDetailsRepository extends JpaRepository<AssignmentDetails, Long> {

    Page<AssignmentDetails> findAll(Pageable pageable);
    Optional<AssignmentDetails> findByAssessmentId(Long assessmentId);

    
}

