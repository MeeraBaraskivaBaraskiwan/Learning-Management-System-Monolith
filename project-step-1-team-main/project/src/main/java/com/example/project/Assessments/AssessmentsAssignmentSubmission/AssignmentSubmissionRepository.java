package com.example.project.Assessments.AssessmentsAssignmentSubmission;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {

    Page<AssignmentSubmission> findAll(Pageable pageable);
    Page<AssignmentSubmission> findByAssignment_Id(Long assignmentId, Pageable pageable);
    Page<AssignmentSubmission> findByStudent_Id(Long studentId, Pageable pageable);
    Optional<AssignmentSubmission> findByAssignment_IdAndStudent_Id(Long assignmentId, Long studentId);


}

