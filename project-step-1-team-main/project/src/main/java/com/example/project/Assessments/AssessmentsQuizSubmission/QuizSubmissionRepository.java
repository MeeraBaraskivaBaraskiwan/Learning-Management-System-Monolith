package com.example.project.Assessments.AssessmentsQuizSubmission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {

    Page<QuizSubmission> findByQuizId(Long quizId, Pageable pageable);

    Page<QuizSubmission> findByStudentId(Long studentId, Pageable pageable);

    QuizSubmission findByQuizIdAndStudentId(Long quizId, Long studentId);
}
