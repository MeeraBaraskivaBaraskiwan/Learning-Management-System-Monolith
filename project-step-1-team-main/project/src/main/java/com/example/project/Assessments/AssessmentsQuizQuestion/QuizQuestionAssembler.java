package com.example.project.Assessments.AssessmentsQuizQuestion;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetailsController;
import com.example.project.Assessments.AssessmentsQuizOption.QuizOptionController;

@Component
public class QuizQuestionAssembler implements RepresentationModelAssembler<QuizQuestionDTO, EntityModel<QuizQuestionDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(QuizQuestionAssembler.class);

    @Override
    public EntityModel<QuizQuestionDTO> toModel(QuizQuestionDTO quizQuestionDTO) {
        if (quizQuestionDTO == null) {
            logger.warn("Attempted to assemble a null QuizQuestionDTO");
            return null;
        }

        logger.info("Assembling EntityModel for QuizQuestionDTO with ID: {}", quizQuestionDTO.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<QuizQuestionDTO> entityModel = EntityModel.of(quizQuestionDTO,
                linkTo(methodOn(QuizQuestionController.class).getQuizQuestionById(quizQuestionDTO.getId())).withSelfRel(),
                linkTo(methodOn(QuizQuestionController.class).getAllQuizQuestions(defaultPageable)).withRel("all-quiz-questions"),
                linkTo(methodOn(QuizQuestionController.class).getQuizQuestionsByQuizId(quizQuestionDTO.getQuizId(), defaultPageable)).withRel("quiz-questions"),
                linkTo(methodOn(QuizDetailsController.class).getQuizDetailsById(quizQuestionDTO.getQuizId())).withRel("quiz-details"),
                linkTo(methodOn(QuizOptionController.class).getQuizOptionsByQuestionId(quizQuestionDTO.getId(), defaultPageable)).withRel("quiz-options")
        );

        logger.debug("Added links to EntityModel for QuizQuestionDTO with ID: {}", quizQuestionDTO.getId());
        logger.info("Completed assembling EntityModel for QuizQuestionDTO with ID: {}", quizQuestionDTO.getId());

        return entityModel;
    }
}

