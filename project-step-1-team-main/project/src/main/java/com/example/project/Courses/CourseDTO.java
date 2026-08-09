package com.example.project.Courses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CourseDTO {
    private Long id;

    @NotBlank(message = "Course code must not be blank")
    private String code;

    @NotBlank(message = "Course name must not be blank")
    private String name;
      
    @NotBlank(message = "Course description must not be blank")
    private String description;

    @Min(value = 1, message = "Credits must be at least 1")
    private int credits;
}
