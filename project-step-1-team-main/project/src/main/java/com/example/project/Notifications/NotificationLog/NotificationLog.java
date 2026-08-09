package com.example.project.Notifications.NotificationLog;

import java.time.LocalDateTime;

import com.example.project.Notifications.Notification.Notification;
import com.example.project.Notifications.enums.NotificationStatus;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import com.example.project.Users.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
@Table(name = "notification_logs")

public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;  

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;  

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now(); 

    private String errorMessage;  

}
