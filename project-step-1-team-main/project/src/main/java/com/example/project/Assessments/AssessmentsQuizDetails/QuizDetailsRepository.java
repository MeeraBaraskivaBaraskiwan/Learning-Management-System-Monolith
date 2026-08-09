package com.example.project.Assessments.AssessmentsQuizDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizDetailsRepository extends JpaRepository<QuizDetails, Long> {

    Optional<QuizDetails> findByAssessmentId(Long assessmentId);
    boolean existsByAssessmentId(Long assessmentId);
}