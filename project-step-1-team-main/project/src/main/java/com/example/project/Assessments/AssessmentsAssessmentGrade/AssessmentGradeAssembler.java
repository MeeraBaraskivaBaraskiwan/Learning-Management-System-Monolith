package com.example.project.Assessments.AssessmentsAssessmentGrade;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmissionController;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmissionController;

@Component
public class AssessmentGradeAssembler implements RepresentationModelAssembler<AssessmentGradeDTO, EntityModel<AssessmentGradeDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentGradeAssembler.class);

    @Override
    public EntityModel<AssessmentGradeDTO> toModel(AssessmentGradeDTO dto) {
        if (dto == null) {
            logger.warn("Attempted to assemble a null AssessmentGradeDTO");
            return null;
        }

        logger.info("Assembling EntityModel for AssessmentGradeDTO with ID: {}", dto.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<AssessmentGradeDTO> model = EntityModel.of(dto,
            linkTo(methodOn(AssessmentGradeController.class).getGradeById(dto.getId())).withSelfRel(),
            linkTo(methodOn(AssessmentGradeController.class).getAllGrades(defaultPageable)).withRel("all-grades"),
            linkTo(methodOn(AssessmentGradeController.class).getGradesByStudentId(dto.getStudentId(), defaultPageable)).withRel("student-grades"),
            linkTo(methodOn(AssessmentGradeController.class).getGradesByAssessmentId(dto.getAssessmentId(), defaultPageable)).withRel("assessment-grades"),
            linkTo(methodOn(AssessmentGradeController.class).getGradeByAssessmentAndStudent(dto.getAssessmentId(), dto.getStudentId())).withRel("by-assessment-student")
        );

        logger.debug("Added basic links to EntityModel for AssessmentGradeDTO with ID: {}", dto.getId());

        if (dto.getQuizSubmissionId() != null) {
            model.add(linkTo(methodOn(QuizSubmissionController.class).getQuizSubmissionById(dto.getQuizSubmissionId()))
                .withRel("quiz-submission"));
            logger.debug("Added quiz-submission link to EntityModel for AssessmentGradeDTO with ID: {}", dto.getId());
        }

        if (dto.getAssignmentSubmissionId() != null) {
            model.add(linkTo(methodOn(AssignmentSubmissionController.class).getAssignmentSubmissionById(dto.getAssignmentSubmissionId()))
                .withRel("assignment-submission"));
            logger.debug("Added assignment-submission link to EntityModel for AssessmentGradeDTO with ID: {}", dto.getId());
        }

        logger.info("Completed assembling EntityModel for AssessmentGradeDTO with ID: {}", dto.getId());
        return model;
    }
}
