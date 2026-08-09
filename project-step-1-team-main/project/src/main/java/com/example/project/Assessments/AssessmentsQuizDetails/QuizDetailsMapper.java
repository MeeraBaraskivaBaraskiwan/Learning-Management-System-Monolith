package com.example.project.Assessments.AssessmentsQuizDetails;

import com.example.project.Assessments.Assessments_Assessment.Assessment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuizDetailsMapper {

    private static final Logger logger = LoggerFactory.getLogger(QuizDetailsMapper.class);

    public static QuizDetailsDTO toDTO(QuizDetails quizDetails) {
        if (quizDetails == null) {
            logger.warn("Attempted to map a null QuizDetails to QuizDetailsDTO");
            return null;
        }

        logger.info("Mapping QuizDetails to QuizDetailsDTO with ID: {}", quizDetails.getId());

        QuizDetailsDTO dto = new QuizDetailsDTO();
        dto.setId(quizDetails.getId());
        dto.setAssessmentId(quizDetails.getAssessment().getId());
        dto.setOpenTime(quizDetails.getOpenTime());
        dto.setClosingTime(quizDetails.getClosingTime());
        dto.setTimeLimitMinutes(quizDetails.getTimeLimitMinutes());
        dto.setPublished(quizDetails.isPublished());
        dto.setTotalScore(quizDetails.getTotalScore());

        logger.debug("Mapped QuizDetailsDTO: {}", dto);
        return dto;
    }

    public static QuizDetails toEntity(QuizDetailsDTO dto, Assessment assessment) {
        if (dto == null) {
            logger.warn("Attempted to map a null QuizDetailsDTO to QuizDetails");
            return null;
        }
        if (assessment == null) {
            logger.error("Assessment is null while mapping QuizDetailsDTO to QuizDetails");
            return null;
        }

        logger.info("Mapping QuizDetailsDTO to QuizDetails with Assessment ID: {}", assessment.getId());

        QuizDetails quizDetails = new QuizDetails(
            assessment,
            dto.getOpenTime(),
            dto.getClosingTime(),
            dto.getTimeLimitMinutes(),
            dto.isPublished(),
            dto.getTotalScore()
        );

        logger.debug("Mapped QuizDetails: {}", quizDetails);
        return quizDetails;
    }
}

