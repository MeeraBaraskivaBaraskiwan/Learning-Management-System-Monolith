package com.example.project.Assessments.AssessmentsQuizSubmission;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

public interface QuizSubmissionService {

    PagedModel<EntityModel<QuizSubmissionDTO>> getAllQuizSubmissions(Pageable pageable);

    EntityModel<QuizSubmissionDTO> getQuizSubmissionById(Long id);

    PagedModel<EntityModel<QuizSubmissionDTO>> getQuizSubmissionsByQuizId(Long quizId, Pageable pageable);

    PagedModel<EntityModel<QuizSubmissionDTO>> getQuizSubmissionsByStudentId(Long studentId, Pageable pageable);

    ResponseEntity<?> createQuizSubmission(QuizSubmissionDTO dto);

    ResponseEntity<?> updateQuizSubmission(Long id, QuizSubmissionDTO dto);

    ResponseEntity<?> deleteQuizSubmission(Long id);
}
