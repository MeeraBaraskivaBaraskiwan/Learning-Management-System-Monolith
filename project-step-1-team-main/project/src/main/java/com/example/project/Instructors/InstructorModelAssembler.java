package com.example.project.Instructors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class InstructorModelAssembler implements RepresentationModelAssembler<InstructorDTO, EntityModel<InstructorDTO>> {

    @Override
    public EntityModel<InstructorDTO> toModel(InstructorDTO instructor) {
        return EntityModel.of(instructor,
                linkTo(methodOn(InstructorController.class).one(instructor.getId())).withSelfRel(),
                linkTo(methodOn(InstructorController.class).all(null)).withRel("instructors"));
    }
}
