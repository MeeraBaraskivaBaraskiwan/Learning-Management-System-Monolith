package com.example.project.Assessments.AssessmentsQuizOption;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {

    Page<QuizOption> findByQuestionId(Long questionId, Pageable pageable);}
