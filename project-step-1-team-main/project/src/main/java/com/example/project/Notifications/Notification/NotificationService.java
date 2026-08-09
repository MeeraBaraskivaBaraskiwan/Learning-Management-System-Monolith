package com.example.project.Notifications.Notification;

import org.springframework.data.domain.Pageable; 
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface NotificationService {
    ResponseEntity<EntityModel<NotificationDTO>> createNotification(NotificationDTO notificationDTO);

    EntityModel<NotificationDTO> getNotificationById(Long id);

    PagedModel<EntityModel<NotificationDTO>> getAllNotifications(Pageable pageable);

    ResponseEntity<?> updateNotification(Long id, NotificationDTO notificationDTO);

    ResponseEntity<?> deleteNotification(Long id);
}