package com.example.project.Enrollments;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.project.Progress.ProgressDTO;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Long id;

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    @NotNull(message = "Section ID cannot be null")
    private Long sectionId;

    private boolean completed;

    @Min(value = 0, message = "Current progress cannot be negative")
    @Max(value = 100, message = "Current progress cannot exceed 100")
    private double currentProgress;

    private List<ProgressDTO> progressRecords;
}