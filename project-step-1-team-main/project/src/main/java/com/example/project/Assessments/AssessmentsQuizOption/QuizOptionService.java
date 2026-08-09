package com.example.project.Assessments.AssessmentsQuizOption;


import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface QuizOptionService {

    PagedModel<EntityModel<QuizOptionDTO>> getAllQuizOptions(Pageable pageable);

    EntityModel<QuizOptionDTO> getQuizOptionById(Long id);

    PagedModel<EntityModel<QuizOptionDTO>> getQuizOptionsByQuestionId(Long questionId, Pageable pageable);

    ResponseEntity<?> createQuizOption(QuizOptionDTO dto);

    ResponseEntity<?> updateQuizOption(Long id, QuizOptionDTO dto);

    ResponseEntity<?> deleteQuizOption(Long id);
}

