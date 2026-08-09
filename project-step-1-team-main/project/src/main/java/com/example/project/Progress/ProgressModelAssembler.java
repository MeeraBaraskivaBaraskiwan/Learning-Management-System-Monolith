package com.example.project.Progress;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProgressModelAssembler implements RepresentationModelAssembler<ProgressDTO, EntityModel<ProgressDTO>> {

    @Override
    public EntityModel<ProgressDTO> toModel(ProgressDTO progress) {
        return EntityModel.of(progress,
                linkTo(methodOn(ProgressController.class).getProgressByEnrollment(progress.getEnrollmentId())).withRel("progress"),
                linkTo(methodOn(ProgressController.class).deleteProgress(progress.getId())).withRel("delete"));
    }
}
