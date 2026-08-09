package com.example.project.Assessments.AssessmentsQuizDetails;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuizDetailsDTO {

    private Long id;

    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;

    @NotNull(message = "Open time is required")
    @FutureOrPresent(message = "Open time must be in the present or future")
    private LocalDateTime openTime;

    @NotNull(message = "Closing time is required")
    @Future(message = "Closing time must be in the future")
    private LocalDateTime closingTime;

    @NotNull(message = "Time limit is required")
    @Positive(message = "Time limit must be a positive number")
    private Integer timeLimitMinutes;

    private boolean published;

    @NotNull(message = "Total score is required")
    @Positive(message = "Total score must be a positive number")
    private Double totalScore;
}

