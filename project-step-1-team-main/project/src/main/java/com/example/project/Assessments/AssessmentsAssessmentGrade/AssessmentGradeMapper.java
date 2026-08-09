package com.example.project.Assessments.AssessmentsAssessmentGrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmission;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Students.Student;

@Component
public class AssessmentGradeMapper {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentGradeMapper.class);

    public static AssessmentGradeDTO toDTO(AssessmentGrade grade) {
        if (grade == null) {
            logger.warn("Attempted to map a null AssessmentGrade to AssessmentGradeDTO");
            return null;
        }

        logger.info("Mapping AssessmentGrade to AssessmentGradeDTO with ID: {}", grade.getId());

        AssessmentGradeDTO dto = new AssessmentGradeDTO();
        dto.setId(grade.getId());
        dto.setAssessmentId(grade.getAssessment().getId());
        dto.setStudentId(grade.getStudent().getId());
        dto.setQuizSubmissionId(grade.getQuizSubmission() != null ? grade.getQuizSubmission().getId() : null);
        dto.setAssignmentSubmissionId(grade.getAssignmentSubmission() != null ? grade.getAssignmentSubmission().getId() : null);
        dto.setAutoGradedScore(grade.getAutoGradedScore());
        dto.setFinalScore(grade.getFinalScore());
        dto.setGradingComments(grade.getGradingComments());
        dto.setFullyGraded(grade.isFullyGraded());

        logger.debug("Mapped AssessmentGradeDTO: {}", dto);
        return dto;
    }

    public static AssessmentGrade toEntity(
        AssessmentGradeDTO dto,
        Assessment assessment,
        Student student,
        QuizSubmission quizSubmission,
        AssignmentSubmission assignmentSubmission
    ) {
        if (dto == null) {
            logger.warn("Attempted to map a null AssessmentGradeDTO to AssessmentGrade");
            return null;
        }
        if (assessment == null) {
            logger.error("Assessment is null while mapping AssessmentGradeDTO to AssessmentGrade");
            return null;
        }
        if (student == null) {
            logger.error("Student is null while mapping AssessmentGradeDTO to AssessmentGrade");
            return null;
        }

        logger.info("Mapping AssessmentGradeDTO to AssessmentGrade for Assessment ID: {} and Student ID: {}", assessment.getId(), student.getId());

        AssessmentGrade grade = new AssessmentGrade();
        grade.setId(dto.getId());
        grade.setAssessment(assessment);
        grade.setStudent(student);
        grade.setQuizSubmission(quizSubmission);
        grade.setAssignmentSubmission(assignmentSubmission);
        grade.setAutoGradedScore(dto.getAutoGradedScore());
        grade.setFinalScore(dto.getFinalScore());
        grade.setGradingComments(dto.getGradingComments());
        grade.setFullyGraded(dto.isFullyGraded());

        logger.debug("Mapped AssessmentGrade: {}", grade);
        return grade;
    }
}
