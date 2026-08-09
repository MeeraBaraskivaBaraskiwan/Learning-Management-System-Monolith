package com.example.project.CourseInstructors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CourseInstructorModelAssembler implements RepresentationModelAssembler<CourseInstructorDTO, EntityModel<CourseInstructorDTO>> {

    @Override
    public EntityModel<CourseInstructorDTO> toModel(CourseInstructorDTO courseInstructor) {
        return EntityModel.of(courseInstructor,
                linkTo(methodOn(CourseInstructorController.class).one(courseInstructor.getId())).withSelfRel(),
                linkTo(methodOn(CourseInstructorController.class).all(null)).withRel("course-instructors"));
    }
}
