package com.example.project.Assessments.AssessmentsQuizQuestion;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;

public interface QuizQuestionService {

    PagedModel<EntityModel<QuizQuestionDTO>> getAllQuizQuestions(Pageable pageable);

    EntityModel<QuizQuestionDTO> getQuizQuestionById(Long id);

    PagedModel<EntityModel<QuizQuestionDTO>> getQuizQuestionsByQuizId(Long quizId, Pageable pageable);

    ResponseEntity<?> createQuizQuestion(QuizQuestionDTO dto);

    ResponseEntity<?> updateQuizQuestion(Long id, QuizQuestionDTO dto);

    ResponseEntity<?> deleteQuizQuestion(Long id);
}
