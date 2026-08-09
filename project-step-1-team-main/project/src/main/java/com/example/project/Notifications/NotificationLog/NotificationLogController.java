package com.example.project.Notifications.NotificationLog;

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

@Tag(name = "Notification Logs", description = "Log entries for user notifications")
@RestController
@RequestMapping("/notification-logs")
public class NotificationLogController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationLogController.class);

    private final NotificationLogService notificationLogService;

    public NotificationLogController(NotificationLogService notificationLogService) {
        this.notificationLogService = notificationLogService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Create a new notification log")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Log created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<EntityModel<NotificationLogDTO>> createLog(@Valid @RequestBody NotificationLogDTO dto) {
        logger.info("Creating a new notification log for notification ID: {}", dto.getNotificationId());
        ResponseEntity<EntityModel<NotificationLogDTO>> response = notificationLogService.createLog(dto);
        logger.info("Successfully created notification log for notification ID: {}", dto.getNotificationId());
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get a log by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log found"),
        @ApiResponse(responseCode = "404", description = "Log not found")
    })
    @GetMapping("/{id}")
    public EntityModel<NotificationLogDTO> getLogById(@PathVariable Long id) {
        logger.info("Fetching notification log with ID: {}", id);
        EntityModel<NotificationLogDTO> log = notificationLogService.getLogById(id);
        logger.info("Successfully fetched notification log with ID: {}", id);
        return log;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all logs (paginated)")
    @ApiResponse(responseCode = "200", description = "Logs retrieved")
    @GetMapping
    public PagedModel<EntityModel<NotificationLogDTO>> getAllLogs(
            @PageableDefault(size = 5, sort = "timestamp", direction = Sort.Direction.DESC)
            Pageable pageable) {
        logger.info("Fetching all notification logs with pageable: {}", pageable);
        PagedModel<EntityModel<NotificationLogDTO>> logs = notificationLogService.getAllLogs(pageable);
        logger.info("Successfully fetched all notification logs");
        return logs;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all logs (default paging)")
    @ApiResponse(responseCode = "200", description = "Logs retrieved")
    @GetMapping("/default")
    public PagedModel<EntityModel<NotificationLogDTO>> getAllLogsNoArgs() {
        logger.info("Fetching all notification logs with default paging");
        Pageable defaultPageable = PageRequest.of(0, 5, Sort.by("timestamp").descending());
        PagedModel<EntityModel<NotificationLogDTO>> logs = notificationLogService.getAllLogs(defaultPageable);
        logger.info("Successfully fetched all notification logs with default paging");
        return logs;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update log by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Log updated"),
        @ApiResponse(responseCode = "404", description = "Log not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLog(@PathVariable Long id, @Valid @RequestBody NotificationLogDTO dto) {
        logger.info("Updating notification log with ID: {}", id);
        ResponseEntity<?> response = notificationLogService.updateLog(id, dto);
        logger.info("Successfully updated notification log with ID: {}", id);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete log by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Log deleted"),
        @ApiResponse(responseCode = "404", description = "Log not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable Long id) {
        logger.info("Deleting notification log with ID: {}", id);
        ResponseEntity<?> response = notificationLogService.deleteLog(id);
        logger.info("Successfully deleted notification log with ID: {}", id);
        return response;
    }
}