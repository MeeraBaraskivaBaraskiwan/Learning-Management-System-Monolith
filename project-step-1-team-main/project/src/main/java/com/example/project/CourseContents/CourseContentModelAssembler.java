package com.example.project.CourseContents;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class CourseContentModelAssembler implements RepresentationModelAssembler<CourseContentDTO, EntityModel<CourseContentDTO>> {

    @Override
    public EntityModel<CourseContentDTO> toModel(CourseContentDTO courseContent) {
        return EntityModel.of(courseContent,
                linkTo(methodOn(CourseContentController.class).one(courseContent.getId())).withSelfRel(),
                linkTo(methodOn(CourseContentController.class).all(null)).withRel("course-contents"));
    }
}
