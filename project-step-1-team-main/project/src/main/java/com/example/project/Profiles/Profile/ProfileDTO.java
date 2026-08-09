package com.example.project.Profiles.Profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class ProfileDTO {
    private Long id;

    private Long userId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{8,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotBlank(message = "Country is required")
    private String country;

    private String profilePictureUrl;

    private String bio;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    private String nationality;

    private String gender;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}