package com.example.project.Profiles.InstructorProfile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstructorProfileDTO {
    private Long id;

    @NotNull(message = "Profile ID is required")
    private Long profileId;

    @NotNull(message = "Instructor ID is required")
    private Long instructorId;

    @NotBlank(message = "Faculty is required")
    private String faculty;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Academic rank is required")
    private String academicRank;
}