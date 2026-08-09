package com.example.project.Notifications.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class NotificationAssembler implements RepresentationModelAssembler<NotificationDTO, EntityModel<NotificationDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationAssembler.class);

    @Override
    public EntityModel<NotificationDTO> toModel(NotificationDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null NotificationDTO into an EntityModel");
            return null;
        }

        logger.info("Assembling EntityModel for NotificationDTO with ID: {}", dto.getId());
        EntityModel<NotificationDTO> model = EntityModel.of(dto,
            linkTo(methodOn(NotificationController.class).getNotificationById(dto.getId())).withSelfRel(),
            linkTo(methodOn(NotificationController.class).getAllNotificationsNoArgs()).withRel("all-notifications")
        );
        logger.info("Successfully assembled EntityModel for NotificationDTO with ID: {}", dto.getId());
        return model;
    }
}