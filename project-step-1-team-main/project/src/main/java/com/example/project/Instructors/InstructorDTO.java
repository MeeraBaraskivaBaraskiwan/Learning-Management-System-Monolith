package com.example.project.Instructors;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InstructorDTO {
    private Long id;

    @NotBlank(message = "Instructor ID is required.")
    private String instructorId;

    @NotNull(message = "User ID is required.")
    private Long userId; 

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is required.") 
    private String email;

    @NotBlank(message = "Faculty is required.")
    private String faculty;

    @NotBlank(message = "Department is required.")
    private String department;
}