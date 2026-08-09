package com.example.project.Assessments.AssessmentsQuizOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizOptionDTO {

    private Long id;

    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Option text is required")
    private String optionText;

    private boolean isCorrect;
}

