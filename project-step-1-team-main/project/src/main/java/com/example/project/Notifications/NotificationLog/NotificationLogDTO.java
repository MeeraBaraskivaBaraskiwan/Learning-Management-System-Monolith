package com.example.project.Notifications.NotificationLog;

import com.example.project.Notifications.enums.NotificationStatus;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificationLogDTO {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Notification ID is required")
    private Long notificationId;

    @NotNull(message = "Status is required")
    private NotificationStatus status;


    private LocalDateTime timestamp;

    
    private String errorMessage;
}
