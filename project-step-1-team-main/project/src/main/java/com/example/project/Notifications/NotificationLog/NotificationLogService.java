package com.example.project.Notifications.NotificationLog;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;


public interface NotificationLogService {
    ResponseEntity<EntityModel<NotificationLogDTO>> createLog(NotificationLogDTO dto);
    EntityModel<NotificationLogDTO> getLogById(Long id);
    PagedModel<EntityModel<NotificationLogDTO>> getAllLogs(Pageable pageable);
    ResponseEntity<?> updateLog(Long id, NotificationLogDTO dto);
    ResponseEntity<?> deleteLog(Long id);
}