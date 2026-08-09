package com.example.project.Profiles.StudentProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class StudentProfileAssembler implements RepresentationModelAssembler<StudentProfileDTO, EntityModel<StudentProfileDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileAssembler.class);

    @Override
    public EntityModel<StudentProfileDTO> toModel(StudentProfileDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null StudentProfileDTO into an EntityModel");
            return null;
        }

        logger.info("Assembling EntityModel for StudentProfileDTO with ID: {}", dto.getId());
        EntityModel<StudentProfileDTO> model = EntityModel.of(dto,
            linkTo(methodOn(StudentProfileController.class).getStudentProfileById(dto.getId())).withSelfRel(),
            linkTo(methodOn(StudentProfileController.class).getAllStudentProfiles(null)).withRel("student-profiles")
        );
        logger.info("Successfully assembled EntityModel for StudentProfileDTO with ID: {}", dto.getId());
        return model;
    }
}