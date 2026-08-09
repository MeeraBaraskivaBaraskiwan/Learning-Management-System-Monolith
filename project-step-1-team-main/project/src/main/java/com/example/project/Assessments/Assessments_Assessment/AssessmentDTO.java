package com.example.project.Assessments.Assessments_Assessment;

import java.time.LocalDateTime;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AssessmentDTO {

    private Long id;

    @NotBlank(message = "Course code is required")
    @Size(max = 20, message = "Course code must not exceed 20 characters")
    private String courseCode;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

@NotNull(message = "Section ID is required")
    private Long sectionId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Assessment type is required")
    private AssessmentType type;

    private LocalDateTime createdAt;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be in the present or future")
    private LocalDateTime dueDate;
}
