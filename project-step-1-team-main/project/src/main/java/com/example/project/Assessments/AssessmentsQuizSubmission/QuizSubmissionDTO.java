package com.example.project.Assessments.AssessmentsQuizSubmission;

import java.time.LocalDateTime;
import com.example.project.Assessments.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmissionDTO {
    private Long id;

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Submission status is required")
    private SubmissionStatus submissionStatus;

    @PastOrPresent(message = "Started at must be in the past or present")
    private LocalDateTime startedAt;

    @PastOrPresent(message = "Submitted at must be in the past or present")
    private LocalDateTime submittedAt;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    private String autoGradedAnswers; // JSON stored as String

    private String manuallyGradedAnswers; // JSON stored as String
private Double autoGradedScore;

}


   
