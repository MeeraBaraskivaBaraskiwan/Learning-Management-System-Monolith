package com.example.project.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(UserModelAssembler.class);

    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO) {
        logger.info("Assembling EntityModel for UserDTO with ID: {}", userDTO.getId());
        return EntityModel.of(userDTO,
            linkTo(methodOn(UserController.class).getUserById(userDTO.getId())).withSelfRel(),
            linkTo(methodOn(UserController.class).getAllUsers(null)).withRel("users")
        );
    }
}