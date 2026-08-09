package com.example.project.Notifications.NotificationLog;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Notifications.Notification.Notification;
import com.example.project.Notifications.Notification.NotificationRepository;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationLogServiceImpl implements NotificationLogService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationLogServiceImpl.class);

    private final NotificationLogRepository notificationLogRepository;
    private final NotificationRepository notificationRepository; 
    private final UserRepository userRepository;
    private final NotificationLogAssembler assembler;
    private final PagedResourcesAssembler<NotificationLogDTO> pagedAssembler;

    public NotificationLogServiceImpl(
            NotificationLogRepository notificationLogRepository,
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            NotificationLogAssembler assembler,
            PagedResourcesAssembler<NotificationLogDTO> pagedAssembler
    ) {
        this.notificationLogRepository = notificationLogRepository;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<NotificationLogDTO>> createLog(NotificationLogDTO dto) {
        logger.info("Creating notification log for user ID: {} and notification ID: {}", dto.getUserId(), dto.getNotificationId());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found", dto.getUserId());
                    return new ResourceNotFoundException("User with ID " + dto.getUserId() + " not found");
                });
        Notification notification = notificationRepository.findById(dto.getNotificationId())
                .orElseThrow(() -> {
                    logger.error("Notification with ID {} not found", dto.getNotificationId());
                    return new ResourceNotFoundException("Notification with ID " + dto.getNotificationId() + " not found");
                });
        NotificationLog logEntity = NotificationLogMapper.toEntity(dto, user, notification);
        NotificationLog saved = notificationLogRepository.save(logEntity);
        logger.info("Notification log created with ID: {}", saved.getId());
        EntityModel<NotificationLogDTO> entityModel = assembler.toModel(NotificationLogMapper.toDTO(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @Override
    public EntityModel<NotificationLogDTO> getLogById(Long id) {
        logger.info("Retrieving notification log with ID: {}", id);
        NotificationLog log = notificationLogRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notification log with ID {} not found", id);
                    return new ResourceNotFoundException("Notification log with ID " + id + " not found");
                });
        logger.info("Notification log with ID {} retrieved successfully", id);
        return assembler.toModel(NotificationLogMapper.toDTO(log));
    }

    @Override
    public PagedModel<EntityModel<NotificationLogDTO>> getAllLogs(Pageable pageable) {
        logger.info("Retrieving notification logs with pageable: {}", pageable);
        Page<NotificationLogDTO> page = notificationLogRepository.findAll(pageable)
                .map(NotificationLogMapper::toDTO);
        logger.info("Retrieved {} notification logs", page.getTotalElements());
        return pagedAssembler.toModel(page, assembler);
    }

    @Override
    public ResponseEntity<?> updateLog(Long id, NotificationLogDTO dto) {
        logger.info("Attempting to update notification log with ID: {}", id);
        return notificationLogRepository.findById(id).map(log -> {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> {
                        logger.error("User with ID {} not found", dto.getUserId());
                        return new ResourceNotFoundException("User with ID " + dto.getUserId() + " not found");
                    });
            Notification notification = notificationRepository.findById(dto.getNotificationId())
                    .orElseThrow(() -> {
                        logger.error("Notification with ID {} not found", dto.getNotificationId());
                        return new ResourceNotFoundException("Notification with ID " + dto.getNotificationId() + " not found");
                    });
            log.setUser(user);
            log.setNotification(notification);
            log.setStatus(dto.getStatus());
            log.setErrorMessage(dto.getErrorMessage());
            notificationLogRepository.save(log);
            logger.info("Notification log with ID {} updated successfully.", id);
            EntityModel<NotificationLogDTO> entityModel = assembler.toModel(NotificationLogMapper.toDTO(log));
            return ResponseEntity.ok(entityModel);
        }).orElseThrow(() -> {
            logger.error("Notification log with ID {} not found for update.", id);
            return new ResourceNotFoundException("Notification log with ID " + id + " not found");
        });
    }

    @Override
    public ResponseEntity<?> deleteLog(Long id) {
        logger.info("Attempting to delete notification log with ID: {}", id);
        return notificationLogRepository.findById(id).map(log -> {
            notificationLogRepository.delete(log);
            logger.info("Notification log with ID {} deleted successfully.", id);
            return ResponseEntity.noContent().build();
        }).orElseThrow(() -> {
            logger.error("Notification log with ID {} not found for deletion.", id);
            return new ResourceNotFoundException("Notification log with ID " + id + " not found");
        });
    }
}