package com.example.project.Notifications.Notification;

import com.example.project.Notifications.enums.NotificationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.example.project.Users.User;

@Component
public class NotificationMapper {

    private static final Logger logger = LoggerFactory.getLogger(NotificationMapper.class);

    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            logger.warn("Attempted to map a null Notification entity to DTO");
            return null;
        }

        logger.info("Mapping Notification entity with ID: {} to DTO", notification.getId());
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRecipientId(notification.getRecipient().getId());
        dto.setType(notification.getType());
        dto.setChannel(notification.getChannel());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setSentAt(notification.getSentAt());
        logger.info("Successfully mapped Notification entity with ID: {} to DTO", notification.getId());
        return dto;
    }

    public static Notification toEntity(NotificationDTO dto, User recipient) {
        if (dto == null || recipient == null) {
            logger.warn("Attempted to map a null NotificationDTO or User to Notification entity");
            return null;
        }

        logger.info("Mapping NotificationDTO with ID: {} to Notification entity", dto.getId());
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(dto.getType());
        notification.setChannel(dto.getChannel());
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setStatus(dto.getStatus() != null ? dto.getStatus() : NotificationStatus.PENDING);
        logger.info("Successfully mapped NotificationDTO with ID: {} to Notification entity", dto.getId());
        return notification;
    }
}