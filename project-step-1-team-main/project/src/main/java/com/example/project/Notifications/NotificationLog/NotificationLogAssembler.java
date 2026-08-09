package com.example.project.Notifications.NotificationLog;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class NotificationLogAssembler implements RepresentationModelAssembler<NotificationLogDTO, EntityModel<NotificationLogDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(NotificationLogAssembler.class);

    @Override
    public EntityModel<NotificationLogDTO> toModel(NotificationLogDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null NotificationLogDTO into an EntityModel");
            return null;
        }

        logger.info("Assembling EntityModel for NotificationLogDTO with ID: {}", dto.getId());
        EntityModel<NotificationLogDTO> model = EntityModel.of(dto,
            linkTo(methodOn(NotificationLogController.class).getLogById(dto.getId())).withSelfRel(),
            linkTo(methodOn(NotificationLogController.class).getAllLogsNoArgs()).withRel("all-notification-logs")
        );
        logger.info("Successfully assembled EntityModel for NotificationLogDTO with ID: {}", dto.getId());
        return model;
    }
}