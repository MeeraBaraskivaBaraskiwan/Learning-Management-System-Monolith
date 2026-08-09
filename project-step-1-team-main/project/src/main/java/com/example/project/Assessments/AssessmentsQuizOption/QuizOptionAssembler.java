package com.example.project.Assessments.AssessmentsQuizOption;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestionController;

@Component
public class QuizOptionAssembler implements RepresentationModelAssembler<QuizOptionDTO, EntityModel<QuizOptionDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(QuizOptionAssembler.class);

    @Override
    public EntityModel<QuizOptionDTO> toModel(QuizOptionDTO quizOptionDTO) {
        if (quizOptionDTO == null) {
            logger.warn("Attempted to assemble a null QuizOptionDTO");
            return null;
        }

        logger.info("Assembling EntityModel for QuizOptionDTO with ID: {}", quizOptionDTO.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<QuizOptionDTO> entityModel = EntityModel.of(quizOptionDTO,
                linkTo(methodOn(QuizOptionController.class).getQuizOptionById(quizOptionDTO.getId())).withSelfRel(),
                linkTo(methodOn(QuizOptionController.class).getAllQuizOptions(defaultPageable)).withRel("all-quiz-options"),
                linkTo(methodOn(QuizOptionController.class).getQuizOptionsByQuestionId(quizOptionDTO.getQuestionId(), defaultPageable)).withRel("question-options"),
                linkTo(methodOn(QuizQuestionController.class).getQuizQuestionById(quizOptionDTO.getQuestionId())).withRel("quiz-question")
        );

        logger.debug("Added links to EntityModel for QuizOptionDTO with ID: {}", quizOptionDTO.getId());
        logger.info("Completed assembling EntityModel for QuizOptionDTO with ID: {}", quizOptionDTO.getId());

        return entityModel;
    }
}
