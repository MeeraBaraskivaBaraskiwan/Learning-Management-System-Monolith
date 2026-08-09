package com.example.project.Students;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class StudentDTO {

    private Long id;

   @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Major is required")
    private String major;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String email;

    private String firstName;

    private String lastName;

    // ← add this:
    private List<String> courses;
}
