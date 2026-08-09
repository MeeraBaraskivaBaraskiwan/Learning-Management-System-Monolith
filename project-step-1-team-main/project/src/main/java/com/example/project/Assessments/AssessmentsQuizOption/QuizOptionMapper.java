package com.example.project.Assessments.AssessmentsQuizOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestion;

@Component
public class QuizOptionMapper {

    private static final Logger logger = LoggerFactory.getLogger(QuizOptionMapper.class);

    public static QuizOptionDTO toDTO(QuizOption quizOption) {
        if (quizOption == null) {
            logger.warn("Attempted to map a null QuizOption to QuizOptionDTO");
            return null;
        }

        logger.info("Mapping QuizOption to QuizOptionDTO with ID: {}", quizOption.getId());

        QuizOptionDTO dto = new QuizOptionDTO();
        dto.setId(quizOption.getId());
        dto.setQuestionId(quizOption.getQuestion().getId());
        dto.setOptionText(quizOption.getOptionText());
        dto.setCorrect(quizOption.isCorrect());

        logger.debug("Mapped QuizOptionDTO: {}", dto);
        return dto;
    }

    public static QuizOption toEntity(QuizOptionDTO dto, QuizQuestion question) {
        if (dto == null) {
            logger.warn("Attempted to map a null QuizOptionDTO to QuizOption");
            return null;
        }
        if (question == null) {
            logger.error("QuizQuestion is null while mapping QuizOptionDTO to QuizOption");
            return null;
        }

        logger.info("Mapping QuizOptionDTO to QuizOption for Question ID: {}", question.getId());

        QuizOption quizOption = new QuizOption();
        quizOption.setQuestion(question);
        quizOption.setOptionText(dto.getOptionText());
        quizOption.setCorrect(dto.isCorrect());

        logger.debug("Mapped QuizOption: {}", quizOption);
        return quizOption;
    }
}
