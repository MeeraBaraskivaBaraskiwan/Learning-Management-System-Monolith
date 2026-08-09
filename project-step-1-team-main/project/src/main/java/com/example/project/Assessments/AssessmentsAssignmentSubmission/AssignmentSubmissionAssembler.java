package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetailsController;
import com.example.project.Files.FileMetadataController;
import com.example.project.Students.StudentController;

@Component
public class AssignmentSubmissionAssembler implements RepresentationModelAssembler<AssignmentSubmissionDTO, EntityModel<AssignmentSubmissionDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentSubmissionAssembler.class);

    @Override
    public EntityModel<AssignmentSubmissionDTO> toModel(AssignmentSubmissionDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null AssignmentSubmissionDTO");
            return null;
        }

        logger.info("Assembling EntityModel for AssignmentSubmissionDTO with ID: {}", dto.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<AssignmentSubmissionDTO> entityModel = EntityModel.of(
            dto,
            linkTo(methodOn(AssignmentSubmissionController.class).getAssignmentSubmissionById(dto.getId())).withSelfRel(),
            linkTo(methodOn(AssignmentSubmissionController.class).getAllAssignmentSubmissions(defaultPageable)).withRel("all-submissions"),
            linkTo(methodOn(AssignmentSubmissionController.class).getSubmissionsByAssignmentId(dto.getAssignmentId(), defaultPageable)).withRel("assignment-submissions"),
            linkTo(methodOn(AssignmentSubmissionController.class).getSubmissionsByStudentId(dto.getStudentId(), defaultPageable)).withRel("student-submissions"),
            linkTo(methodOn(AssignmentSubmissionController.class).getSubmissionByAssignmentIdAndStudentId(dto.getAssignmentId(), dto.getStudentId())).withRel("student-assignment-submission"),
            linkTo(methodOn(AssignmentDetailsController.class).getById(dto.getAssignmentId())).withRel("assignment-details"),
            linkTo(methodOn(StudentController.class).one(dto.getStudentId())).withRel("student")
        );

        logger.debug("Added basic links to EntityModel for AssignmentSubmissionDTO with ID: {}", dto.getId());

        entityModel.add(linkTo(methodOn(FileMetadataController.class)
            .uploadSubmission(dto.getId(), (MultipartFile) null)).withRel("upload-file"));
        logger.debug("Added upload-file link to EntityModel for AssignmentSubmissionDTO with ID: {}", dto.getId());

        entityModel.add(linkTo(methodOn(FileMetadataController.class)
            .getFilesBySubmission(dto.getId(), defaultPageable)).withRel("files-for-submission"));
        logger.debug("Added files-for-submission link to EntityModel for AssignmentSubmissionDTO with ID: {}", dto.getId());

        entityModel.add(linkTo(methodOn(AssignmentSubmissionController.class)
            .addOrUpdateFeedback(dto.getId(), "<feedback>")).withRel("add-feedback"));
        logger.debug("Added add-feedback link to EntityModel for AssignmentSubmissionDTO with ID: {}", dto.getId());

        logger.info("Completed assembling EntityModel for AssignmentSubmissionDTO with ID: {}", dto.getId());

        return entityModel;
    }
}
