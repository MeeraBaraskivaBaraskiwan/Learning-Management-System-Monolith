package com.example.project.Courses;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class CourseModelAssembler implements RepresentationModelAssembler<CourseDTO, EntityModel<CourseDTO>> {

    @Override
    public EntityModel<CourseDTO> toModel(CourseDTO course) {
        return EntityModel.of(course,
                linkTo(methodOn(CourseController.class).one(course.getId())).withSelfRel(),
                linkTo(methodOn(CourseController.class).all(null)).withRel("courses"));
    }
}
