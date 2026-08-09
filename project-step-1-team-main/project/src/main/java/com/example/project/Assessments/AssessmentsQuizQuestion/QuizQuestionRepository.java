package com.example.project.Assessments.AssessmentsQuizQuestion;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    
    Page<QuizQuestion> findByQuizId(Long quizId, Pageable pageable);

}

