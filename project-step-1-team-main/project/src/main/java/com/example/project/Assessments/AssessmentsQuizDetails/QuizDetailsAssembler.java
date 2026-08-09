package com.example.project.Assessments.AssessmentsQuizDetails;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestionController;
import com.example.project.Assessments.Assessments_Assessment.AssessmentController;

@Component
public class QuizDetailsAssembler implements RepresentationModelAssembler<QuizDetailsDTO, EntityModel<QuizDetailsDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(QuizDetailsAssembler.class);

    @Override
    public EntityModel<QuizDetailsDTO> toModel(QuizDetailsDTO quizDetailsDTO) {
        if (quizDetailsDTO == null) {
            logger.warn("Attempted to assemble a null QuizDetailsDTO");
            return null;
        }

        logger.info("Assembling EntityModel for QuizDetailsDTO with ID: {}", quizDetailsDTO.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<QuizDetailsDTO> entityModel = EntityModel.of(quizDetailsDTO,
                linkTo(methodOn(QuizDetailsController.class).getQuizDetailsById(quizDetailsDTO.getId())).withSelfRel(),
                linkTo(methodOn(QuizDetailsController.class).getAllQuizDetails(defaultPageable)).withRel("all-quiz-details"),
                linkTo(methodOn(AssessmentController.class).getAssessmentById(quizDetailsDTO.getAssessmentId())).withRel("assessment"),
                linkTo(methodOn(QuizQuestionController.class).getQuizQuestionsByQuizId(quizDetailsDTO.getId(), defaultPageable)).withRel("quiz-questions")
        );

        logger.debug("Added links to EntityModel for QuizDetailsDTO with ID: {}", quizDetailsDTO.getId());
        logger.info("Completed assembling EntityModel for QuizDetailsDTO with ID: {}", quizDetailsDTO.getId());

        return entityModel;
    }
}
