package com.example.project.Profiles.InstructorProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class InstructorProfileAssembler implements RepresentationModelAssembler<InstructorProfileDTO, EntityModel<InstructorProfileDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(InstructorProfileAssembler.class);

    @Override
    public EntityModel<InstructorProfileDTO> toModel(InstructorProfileDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null InstructorProfileDTO into an EntityModel");
            return null;
        }

        logger.info("Assembling EntityModel for InstructorProfileDTO with ID: {}", dto.getId());
        EntityModel<InstructorProfileDTO> model = EntityModel.of(dto,
                linkTo(methodOn(InstructorProfileController.class).getInstructorProfileById(dto.getId())).withSelfRel(),
                linkTo(methodOn(InstructorProfileController.class).getAllInstructorProfiles(null)).withRel("instructor-profiles")
        );
        logger.info("Successfully assembled EntityModel for InstructorProfileDTO with ID: {}", dto.getId());
        return model;
    }
}