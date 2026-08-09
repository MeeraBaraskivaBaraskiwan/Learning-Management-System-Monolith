package com.example.project.Assessments.AssessmentsQuizQuestion;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionDTO {
  private Long id;

    @NotNull(message = "Quiz ID is required")
    private Long quizId;

    private int questionNumber;

    @NotNull(message = "Score is required")
    @PositiveOrZero(message = "Score must be non-negative")
    private Double score;

    @NotBlank(message = "Question text is required")
    private String questionText;

    @NotNull(message = "Question type is required")
    private QuestionType questionType;

    private boolean isAutoGraded;

    private Long correctOptionId;


}
