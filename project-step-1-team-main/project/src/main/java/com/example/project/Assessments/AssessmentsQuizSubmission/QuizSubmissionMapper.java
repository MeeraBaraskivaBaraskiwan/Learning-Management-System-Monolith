package com.example.project.Assessments.AssessmentsQuizSubmission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Students.Student;

@Component
public class QuizSubmissionMapper {

    private static final Logger logger = LoggerFactory.getLogger(QuizSubmissionMapper.class);

    public static QuizSubmissionDTO toDTO(QuizSubmission quizSubmission) {
        if (quizSubmission == null) {
            logger.warn("Attempted to map a null QuizSubmission to QuizSubmissionDTO");
            return null;
        }

        logger.info("Mapping QuizSubmission to QuizSubmissionDTO with ID: {}", quizSubmission.getId());

        QuizSubmissionDTO dto = new QuizSubmissionDTO();
        dto.setId(quizSubmission.getId());
        dto.setQuizId(quizSubmission.getQuiz().getId());
        dto.setStudentId(quizSubmission.getStudent().getId());
        dto.setSubmissionStatus(quizSubmission.getSubmissionStatus());
        dto.setStartedAt(quizSubmission.getStartedAt());
        dto.setSubmittedAt(quizSubmission.getSubmittedAt());
        dto.setDurationMinutes(quizSubmission.getDurationMinutes());
        dto.setAutoGradedAnswers(quizSubmission.getAutoGradedAnswers());
        dto.setManuallyGradedAnswers(quizSubmission.getManuallyGradedAnswers());
dto.setAutoGradedScore(quizSubmission.getAutoGradedScore());

        logger.debug("Mapped QuizSubmissionDTO: {}", dto);
        return dto;
    }

    public static QuizSubmission toEntity(QuizSubmissionDTO dto, QuizDetails quiz, Student student) {
        if (dto == null) {
            logger.warn("Attempted to map a null QuizSubmissionDTO to QuizSubmission");
            return null;
        }
        if (quiz == null) {
            logger.error("QuizDetails is null while mapping QuizSubmissionDTO to QuizSubmission");
            return null;
        }
        if (student == null) {
            logger.error("Student is null while mapping QuizSubmissionDTO to QuizSubmission");
            return null;
        }

        logger.info("Mapping QuizSubmissionDTO to QuizSubmission for Quiz ID: {} and Student ID: {}", quiz.getId(), student.getId());

        QuizSubmission quizSubmission = new QuizSubmission();
        quizSubmission.setQuiz(quiz);
        quizSubmission.setStudent(student);
        quizSubmission.setSubmissionStatus(dto.getSubmissionStatus());
        quizSubmission.setStartedAt(dto.getStartedAt());
        quizSubmission.setSubmittedAt(dto.getSubmittedAt());
        quizSubmission.setDurationMinutes(dto.getDurationMinutes());
        quizSubmission.setAutoGradedAnswers(dto.getAutoGradedAnswers());
        quizSubmission.setManuallyGradedAnswers(dto.getManuallyGradedAnswers());
quizSubmission.setAutoGradedScore(dto.getAutoGradedScore());

        logger.debug("Mapped QuizSubmission: {}", quizSubmission);
        return quizSubmission;
    }
}
