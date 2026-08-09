package com.example.project.Assessments.AssessmentsQuizQuestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;

@Component
public class QuizQuestionMapper {

    private static final Logger logger = LoggerFactory.getLogger(QuizQuestionMapper.class);

    public static QuizQuestionDTO toDTO(QuizQuestion quizQuestion) {
    if (quizQuestion == null) {
        logger.warn("Attempted to map a null QuizQuestion to QuizQuestionDTO");
        return null;
    }

    logger.info("Mapping QuizQuestion to QuizQuestionDTO with ID: {}", quizQuestion.getId());

    QuizQuestionDTO dto = new QuizQuestionDTO();
    dto.setId(quizQuestion.getId());
    dto.setQuizId(quizQuestion.getQuiz().getId());
    dto.setQuestionNumber(quizQuestion.getQuestionNumber());
    dto.setScore(quizQuestion.getScore());
    dto.setQuestionText(quizQuestion.getQuestionText());
    dto.setQuestionType(quizQuestion.getQuestionType());
    dto.setAutoGraded(quizQuestion.isAutoGraded());
    dto.setCorrectOptionId(quizQuestion.getCorrectOptionId()); // <-- ADD THIS LINE

    logger.debug("Mapped QuizQuestionDTO: {}", dto);
    return dto;
}

public static QuizQuestion toEntity(QuizQuestionDTO dto, QuizDetails quizDetails) {
    if (dto == null) {
        logger.warn("Attempted to map a null QuizQuestionDTO to QuizQuestion");
        return null;
    }
    if (quizDetails == null) {
        logger.error("QuizDetails is null while mapping QuizQuestionDTO to QuizQuestion");
        return null;
    }

    logger.info("Mapping QuizQuestionDTO to QuizQuestion for Quiz ID: {}", quizDetails.getId());

    QuizQuestion quizQuestion = new QuizQuestion();
    quizQuestion.setQuiz(quizDetails);
    quizQuestion.setQuestionNumber(dto.getQuestionNumber());
    quizQuestion.setScore(dto.getScore());
    quizQuestion.setQuestionText(dto.getQuestionText());
    quizQuestion.setQuestionType(dto.getQuestionType());
    quizQuestion.setAutoGraded(dto.isAutoGraded());
    quizQuestion.setCorrectOptionId(dto.getCorrectOptionId()); // <-- ADD THIS LINE

    logger.debug("Mapped QuizQuestion: {}", quizQuestion);
    return quizQuestion;
}

}
