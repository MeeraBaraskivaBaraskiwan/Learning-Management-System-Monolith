package com.example.project.Assessments.AssessmentsQuizDetails;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

public interface QuizDetailsService {

    PagedModel<EntityModel<QuizDetailsDTO>> getAllQuizDetails(Pageable pageable);

    EntityModel<QuizDetailsDTO> getQuizDetailsById(Long id);

    EntityModel<QuizDetailsDTO> getQuizDetailsByAssessmentId(Long assessmentId);

    ResponseEntity<?> createQuizDetails(QuizDetailsDTO quizDetailsDTO);

    ResponseEntity<?> updateQuizDetails(Long id, QuizDetailsDTO quizDetailsDTO);

    ResponseEntity<?> deleteQuizDetails(Long id);
    
}

