package com.example.project.Profiles.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class ProfileAssembler implements RepresentationModelAssembler<ProfileDTO, EntityModel<ProfileDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(ProfileAssembler.class);

    @Override
    public EntityModel<ProfileDTO> toModel(ProfileDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null ProfileDTO into an EntityModel");
            return null;
        }

        logger.info("Assembling EntityModel for ProfileDTO with ID: {}", dto.getId());
        EntityModel<ProfileDTO> model = EntityModel.of(dto,
            linkTo(methodOn(ProfileController.class).getProfileById(dto.getId())).withSelfRel(),
            linkTo(methodOn(ProfileController.class).getAllProfiles(null)).withRel("profiles")
        );
        logger.info("Successfully assembled EntityModel for ProfileDTO with ID: {}", dto.getId());
        return model;
    }
}