package com.example.project.Students;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class StudentModelAssembler implements RepresentationModelAssembler<StudentDTO, EntityModel<StudentDTO>> {


    private static final Logger logger = LoggerFactory.getLogger(StudentModelAssembler.class);


    @Override
    public EntityModel<StudentDTO> toModel(StudentDTO student) {
        logger.info("Assembling EntityModel for student with ID: {}", student.getId());
        EntityModel<StudentDTO> model = EntityModel.of(student,
                linkTo(methodOn(StudentController.class).one(student.getId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).all(null)).withRel("students"));
        logger.info("Successfully assembled EntityModel for student with ID: {}", student.getId());
        return model;
    }

    public CollectionModel<EntityModel<StudentDTO>> toCollectionModel(Collection<EntityModel<StudentDTO>> students) {
        logger.info("Assembling CollectionModel for students");
        CollectionModel<EntityModel<StudentDTO>> collectionModel = CollectionModel.of(students,
                linkTo(methodOn(StudentController.class).all(null)).withSelfRel());
        logger.info("Successfully assembled CollectionModel for students");
        return collectionModel;
    }
}