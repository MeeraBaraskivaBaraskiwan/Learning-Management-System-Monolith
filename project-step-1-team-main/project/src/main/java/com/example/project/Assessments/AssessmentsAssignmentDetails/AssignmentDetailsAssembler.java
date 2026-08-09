package com.example.project.Assessments.AssessmentsAssignmentDetails;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.Assessments.Assessments_Assessment.AssessmentController;
import com.example.project.Files.FileMetadataController;

@Component
public class AssignmentDetailsAssembler 
    implements RepresentationModelAssembler<AssignmentDetailsDTO, EntityModel<AssignmentDetailsDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentDetailsAssembler.class);

    @Override
    public EntityModel<AssignmentDetailsDTO> toModel(AssignmentDetailsDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null AssignmentDetailsDTO");
            return null;
        }

        logger.info("Assembling EntityModel for AssignmentDetailsDTO with ID: {}", dto.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<AssignmentDetailsDTO> entityModel = EntityModel.of(
            dto,
            linkTo(methodOn(AssignmentDetailsController.class).getById(dto.getId())).withSelfRel(),
            linkTo(methodOn(AssignmentDetailsController.class).getAll(defaultPageable)).withRel("all-assignment-details"),
            linkTo(methodOn(AssignmentDetailsController.class).publish(dto.getId())).withRel("publish"),
            linkTo(methodOn(AssignmentDetailsController.class).delete(dto.getId())).withRel("delete"),
            linkTo(methodOn(AssessmentController.class).getAssessmentById(dto.getAssessmentId())).withRel("assessment")
        );

        logger.debug("Added basic links to EntityModel for AssignmentDetailsDTO with ID: {}", dto.getId());

        entityModel.add(
            linkTo(methodOn(FileMetadataController.class).uploadAssignment(dto.getId(), (MultipartFile) null)).withRel("upload-file")
        );

        logger.debug("Added upload-file link to EntityModel for AssignmentDetailsDTO with ID: {}", dto.getId());

        entityModel.add(
            linkTo(methodOn(FileMetadataController.class).getFilesByAssignment(dto.getId(), defaultPageable)).withRel("files-for-assignment")
        );

        logger.debug("Added files-for-assignment link to EntityModel for AssignmentDetailsDTO with ID: {}", dto.getId());

        logger.info("Completed assembling EntityModel for AssignmentDetailsDTO with ID: {}", dto.getId());

        return entityModel;
    }
}
