package com.example.project.Assessments.Assessments_Assessment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetailsController;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetailsController;
import com.example.project.Sections.SectionController;

import org.springframework.data.domain.Pageable;

@Component
public class AssessmentAssembler implements RepresentationModelAssembler<AssessmentDTO, EntityModel<AssessmentDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentAssembler.class);

    @Override
    public EntityModel<AssessmentDTO> toModel(AssessmentDTO assessmentDTO) {
        if (assessmentDTO == null) {
            logger.warn("Attempted to assemble a null AssessmentDTO");
            return null;
        }

        logger.info("Assembling EntityModel for AssessmentDTO with ID: {}", assessmentDTO.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<AssessmentDTO> entityModel = EntityModel.of(assessmentDTO,
            linkTo(methodOn(AssessmentController.class).getAssessmentById(assessmentDTO.getId())).withSelfRel(),
            linkTo(methodOn(AssessmentController.class).getAllAssessments(defaultPageable)).withRel("all-assessments"),
            linkTo(methodOn(AssessmentController.class).getAssessmentsByCourseCode(assessmentDTO.getCourseCode(), defaultPageable)).withRel("course-assessments")
        );

        entityModel.add(linkTo(methodOn(SectionController.class)
           .forCourse(assessmentDTO.getSectionId()))
         .withRel("section"));

        logger.debug("Added basic links to EntityModel for AssessmentDTO with ID: {}", assessmentDTO.getId());

        if (assessmentDTO.getType() == AssessmentType.QUIZ) {
            logger.info("Adding quiz-details link for AssessmentDTO with ID: {}", assessmentDTO.getId());
            entityModel.add(linkTo(methodOn(QuizDetailsController.class)
                .getQuizDetailsByAssessmentId(assessmentDTO.getId())).withRel("quiz-details"));
        }

        if (assessmentDTO.getType() == AssessmentType.ASSIGNMENT) {
            logger.info("Adding assignment-details link for AssessmentDTO with ID: {}", assessmentDTO.getId());
            entityModel.add(linkTo(methodOn(AssignmentDetailsController.class)
                .getByAssessmentId(assessmentDTO.getId())).withRel("assignment-details"));
        }

        logger.debug("Completed assembling EntityModel for AssessmentDTO with ID: {}", assessmentDTO.getId());
        return entityModel;
    }
}
