package com.example.project.Notifications.Notification;

import java.time.LocalDateTime;

import com.example.project.Notifications.enums.NotificationChannel;
import com.example.project.Notifications.enums.NotificationStatus;
import com.example.project.Notifications.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import lombok.Data;

@Data
public class NotificationDTO {

    private Long id;

    @NotNull(message = "Recipient ID is required")
    private Long recipientId;

    @NotNull(message = "Notification type is required")
    private NotificationType type;

    @NotNull(message = "Notification channel is required")
    private NotificationChannel channel;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Message must not be blank")
    private String message;

    private NotificationStatus status;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime sentAt;
}