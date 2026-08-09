package com.example.project.Notifications.UserPrefrence;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserPreferenceAssembler implements RepresentationModelAssembler<UserPreferenceDTO, EntityModel<UserPreferenceDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceAssembler.class);

    @Override
    public EntityModel<UserPreferenceDTO> toModel(UserPreferenceDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null UserPreferenceDTO into an EntityModel");
            return null;
        }

        logger.info("Assembling EntityModel for UserPreferenceDTO with ID: {}", dto.getId());
        EntityModel<UserPreferenceDTO> model = EntityModel.of(dto,
            linkTo(methodOn(UserPreferenceController.class).getPreferenceById(dto.getId())).withSelfRel(),
            linkTo(methodOn(UserPreferenceController.class).getAllPreferencesNoArgs()).withRel("all-preferences")
        );
        logger.info("Successfully assembled EntityModel for UserPreferenceDTO with ID: {}", dto.getId());
        return model;
    }
}
