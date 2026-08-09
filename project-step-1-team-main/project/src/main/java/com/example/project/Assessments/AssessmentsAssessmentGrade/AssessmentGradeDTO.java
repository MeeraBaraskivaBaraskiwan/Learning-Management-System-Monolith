package com.example.project.Assessments.AssessmentsAssessmentGrade;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AssessmentGradeDTO {
    private Long id;

    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private Long quizSubmissionId; 

    private Long assignmentSubmissionId; 

    @DecimalMin(value = "0.0", inclusive = true, message = "Auto graded score must be at least 0")
    private Double autoGradedScore;

    @DecimalMin(value = "0.0", inclusive = true, message = "Final score must be at least 0")
    private Double finalScore;

    private String gradingComments;

    private boolean fullyGraded;
}
