package com.example.project.Notifications.NotificationLog;

import com.example.project.Notifications.Notification.Notification;
import com.example.project.Users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationLogMapper {

    private static final Logger logger = LoggerFactory.getLogger(NotificationLogMapper.class);

    public static NotificationLogDTO toDTO(NotificationLog log) {
        if (log == null) {
            logger.warn("Attempted to map a null NotificationLog entity to DTO");
            return null;
        }

        logger.info("Mapping NotificationLog entity with ID: {} to DTO", log.getId());
        NotificationLogDTO dto = new NotificationLogDTO();
        dto.setId(log.getId());
        dto.setUserId(log.getUser().getId());
        dto.setNotificationId(log.getNotification().getId());
        dto.setStatus(log.getStatus());
        dto.setTimestamp(log.getTimestamp());
        dto.setErrorMessage(log.getErrorMessage());
        logger.info("Successfully mapped NotificationLog entity with ID: {} to DTO", log.getId());
        return dto;
    }

    public static NotificationLog toEntity(NotificationLogDTO dto, User user, Notification notification) {
        if (dto == null || user == null || notification == null) {
            logger.warn("Attempted to map a null NotificationLogDTO, User, or Notification to NotificationLog entity");
            return null;
        }

        logger.info("Mapping NotificationLogDTO with ID: {} to NotificationLog entity", dto.getId());
        NotificationLog log = new NotificationLog();
        log.setUser(user);
        log.setNotification(notification);
        log.setStatus(dto.getStatus());
        log.setErrorMessage(dto.getErrorMessage());
        logger.info("Successfully mapped NotificationLogDTO with ID: {} to NotificationLog entity", dto.getId());
        return log;
    }
}