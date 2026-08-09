package com.example.project.Notifications.Notification;


import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Notifications.EmailService;
import com.example.project.Notifications.enums.NotificationChannel;
import com.example.project.Notifications.enums.NotificationStatus;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

import jakarta.mail.MessagingException;
import reactor.core.publisher.Sinks;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final NotificationAssembler notificationAssembler;
    private final UserRepository userRepository;
    private final PagedResourcesAssembler<NotificationDTO> pagedAssembler;
    private final EmailService emailService;
    private final Sinks.Many<NotificationDTO> notificationSink;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationAssembler notificationAssembler,
                                   UserRepository userRepository,
                                   PagedResourcesAssembler<NotificationDTO> pagedAssembler,
                                   EmailService emailService,
                                    Sinks.Many<NotificationDTO> notificationSink) {
        this.notificationRepository = notificationRepository;
        this.notificationAssembler = notificationAssembler;
        this.userRepository = userRepository;
        this.pagedAssembler = pagedAssembler;
        this.emailService = emailService;
        this.notificationSink = notificationSink;
    }

    @Override
    public ResponseEntity<EntityModel<NotificationDTO>> createNotification(NotificationDTO notificationDTO) {
        logger.info("Creating notification for recipient ID: {}", notificationDTO.getRecipientId());

        User recipient = userRepository.findById(notificationDTO.getRecipientId())
                .orElseThrow(() -> {
                    logger.error("Recipient not found for ID: {}", notificationDTO.getRecipientId());
                    return new ResourceNotFoundException(
                        "User (recipient) with ID " + notificationDTO.getRecipientId() + " not found"
                    );
                });

        Notification notification = NotificationMapper.toEntity(notificationDTO, recipient);

        Notification saved = notificationRepository.save(notification);
        logger.info("Notification created with ID: {}", saved.getId());

               // Publish into our SSE sink
       NotificationDTO dto = NotificationMapper.toDTO(saved);
       notificationSink.tryEmitNext(dto);

        if (saved.getChannel() == NotificationChannel.EMAIL) {
            try {
                emailService.sendNotificationEmail(saved); 
                saved.markAsSent();
                notificationRepository.save(saved);
                logger.info("Email sent successfully for notification {}", saved.getId());
            } catch (MessagingException e) {
                logger.error("Failed to send email: {}", e.getMessage());
                saved.markAsFailed(e.getMessage());
                notificationRepository.save(saved);
            }
        }
        EntityModel<NotificationDTO> entityModel = notificationAssembler.toModel(NotificationMapper.toDTO(saved));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }


    @Override
    public EntityModel<NotificationDTO> getNotificationById(Long id) {
        logger.info("Retrieving notification with ID: {}", id);
        Notification notification = notificationRepository.findById(id)
        .orElseThrow(() -> {
            logger.error("Notification with ID {} not found", id);
            return new ResourceNotFoundException("Notification with ID " + id + " not found");
        });
        logger.info("Notification with ID {} retrieved successfully", id);
        return notificationAssembler.toModel(NotificationMapper.toDTO(notification));
    }

    @Override
    public PagedModel<EntityModel<NotificationDTO>> getAllNotifications(Pageable pageable) {
        logger.info("Retrieving notifications with pageable: {}", pageable);
        Page<NotificationDTO> page = notificationRepository.findAll(pageable)
                .map(NotificationMapper::toDTO);
        logger.info("Retrieved {} notifications", page.getTotalElements());
        return pagedAssembler.toModel(page, notificationAssembler);
    }


    @Override
public ResponseEntity<?> updateNotification(Long id, NotificationDTO notificationDTO) {
    logger.info("Attempting to update notification with ID: {}", id);
    return notificationRepository.findById(id).map(notification -> {
        User recipient = userRepository.findById(notificationDTO.getRecipientId())
                .orElseThrow(() -> {
                    logger.error("Recipient not found for ID: {}", notificationDTO.getRecipientId());
                    return new ResourceNotFoundException(
                        "User (recipient) with ID " + notificationDTO.getRecipientId() + " not found"
                    );
                });
        notification.setRecipient(recipient);
        notification.setType(notificationDTO.getType());
        notification.setChannel(notificationDTO.getChannel());
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setStatus(notificationDTO.getStatus());
        notificationRepository.save(notification);
        logger.info("Notification with ID {} updated successfully.", id);
        EntityModel<NotificationDTO> entityModel = notificationAssembler.toModel(NotificationMapper.toDTO(notification));
        return ResponseEntity.ok(entityModel);
    })  .orElseThrow(() -> {
        logger.error("Notification with ID {} not found for update.", id);
        return new ResourceNotFoundException("Notification with ID " + id + " not found");
    });
}


@Override
public ResponseEntity<?> deleteNotification(Long id) {
    logger.info("Attempting to delete notification with ID: {}", id);
    return notificationRepository.findById(id).map(notification -> {
        notificationRepository.delete(notification);
        logger.info("Notification with ID {} deleted successfully.", id);
        return ResponseEntity.noContent().build();
    }) .orElseThrow(() -> {
        logger.error("Notification with ID {} not found for deletion.", id);
        return new ResourceNotFoundException("Notification with ID " + id + " not found");
    });
    }
}