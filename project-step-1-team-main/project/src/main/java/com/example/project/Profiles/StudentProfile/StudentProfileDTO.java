package com.example.project.Profiles.StudentProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentProfileDTO {

    private Long id;

    @NotNull(message = "Profile ID is required")
    private Long profileId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotBlank(message = "Admitted year is required")
    private String admittedYear;

    @NotBlank(message = "Major is required")
    private String major;

    private String minor;

   @NotBlank(message = "Current semester is required")
    private String currentSemester;

   @NotBlank(message = "Degree program is required")
    private String degreeProgram;

    private String studentLevel;

    private String tawjihiStream;

    private String probationStatus;

    private String department;

    @NotNull(message = "Advisor ID is required")
    private Long advisorId;
}