package com.example.project.Notifications.Notification;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import org.springframework.http.MediaType;

@Tag(name = "Notifications", description = "Operations related to Notifications")
@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "http://localhost:3000") 
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final Sinks.Many<NotificationDTO> notificationSink;

    public NotificationController(NotificationService notificationService,
                                 NotificationRepository notificationRepository,
                                 Sinks.Many<NotificationDTO> notificationSink) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
        this.notificationSink = notificationSink;
    }


    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Create a new notification")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Notification created"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<EntityModel<NotificationDTO>> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        logger.info("Creating a new notification with title: {}", notificationDTO.getTitle());
        ResponseEntity<EntityModel<NotificationDTO>> response = notificationService.createNotification(notificationDTO);
        logger.info("Successfully created notification with title: {}", notificationDTO.getTitle());
        return response;
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Retrieve notification by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification found"),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @GetMapping("/{id}")
    public EntityModel<NotificationDTO> getNotificationById(@PathVariable Long id) {
        logger.info("Fetching notification with ID: {}", id);
        EntityModel<NotificationDTO> notification = notificationService.getNotificationById(id);
        logger.info("Successfully fetched notification with ID: {}", id);
        return notification;
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Retrieve all notifications with pagination")
    @ApiResponse(responseCode = "200", description = "Notifications retrieved")
    @GetMapping
    public PagedModel<EntityModel<NotificationDTO>> getAllNotifications(
            @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        logger.info("Fetching all notifications with pageable: {}", pageable);
        PagedModel<EntityModel<NotificationDTO>> notifications = notificationService.getAllNotifications(pageable);
        logger.info("Successfully fetched all notifications");
        return notifications;
    }


    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Retrieve all notifications (with default paging)")
    @ApiResponse(responseCode = "200", description = "Notifications retrieved")
    @GetMapping("/default")
    public PagedModel<EntityModel<NotificationDTO>> getAllNotificationsNoArgs() {
        logger.info("Fetching all notifications with default paging");
        Pageable defaultPageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        PagedModel<EntityModel<NotificationDTO>> notifications = notificationService.getAllNotifications(defaultPageable);
        logger.info("Successfully fetched all notifications with default paging");
        return notifications;
    }


    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    @Operation(summary = "Update notification by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notification updated"),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotification(@PathVariable Long id, @Valid @RequestBody NotificationDTO notificationDTO) {
        logger.info("Updating notification with ID: {}", id);
        ResponseEntity<?> response = notificationService.updateNotification(id, notificationDTO);
        logger.info("Successfully updated notification with ID: {}", id);
        return response;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete notification by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Notification deleted"),
        @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        logger.info("Deleting notification with ID: {}", id);
        ResponseEntity<?> response = notificationService.deleteNotification(id);
        logger.info("Successfully deleted notification with ID: {}", id);
        return response;
    }

   @CrossOrigin   // ← you can also put this here
    @GetMapping(path = "/stream/user/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
   public Flux<NotificationDTO> streamNotifications(@PathVariable Long userId) {
       return notificationSink.asFlux()
           // only push events for this recipient
           .filter(dto -> dto.getRecipientId().equals(userId));
   }
   
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user/{userId}")
    public List<NotificationDTO> getNotificationsForUser(@PathVariable Long userId) {
        return notificationRepository
                .findByRecipient_Id(userId)                       // JPA method you already have
                .stream()
                .map(NotificationMapper::toDTO)
                .sorted(Comparator.comparing(NotificationDTO::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

}