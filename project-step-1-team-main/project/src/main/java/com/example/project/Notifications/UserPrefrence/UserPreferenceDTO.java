package com.example.project.Notifications.UserPrefrence;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPreferenceDTO {
    
    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    private boolean emailEnabled;

    private boolean smsEnabled;

    @NotBlank(message = "Phone number is required when SMS is enabled")
    private String phoneNumber;
}