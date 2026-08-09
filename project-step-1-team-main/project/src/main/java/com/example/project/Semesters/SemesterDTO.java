package com.example.project.Semesters;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SemesterDTO {
    private Long id;

    @NotBlank(message = "Term is required")
    private String term;

    @Min(value = 2000, message = "Year must be valid")
    private int year;
}
