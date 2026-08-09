package com.example.project.Assessments.AssessmentsQuizSubmission;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetailsController;
import com.example.project.Students.StudentController;

@Component
public class QuizSubmissionAssembler implements RepresentationModelAssembler<QuizSubmissionDTO, EntityModel<QuizSubmissionDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(QuizSubmissionAssembler.class);

    @Override
    public EntityModel<QuizSubmissionDTO> toModel(QuizSubmissionDTO quizSubmissionDTO) {
        if (quizSubmissionDTO == null) {
            logger.warn("Attempted to assemble a null QuizSubmissionDTO");
            return null;
        }

        logger.info("Assembling EntityModel for QuizSubmissionDTO with ID: {}", quizSubmissionDTO.getId());

        Pageable defaultPageable = PageRequest.of(0, 5);

        EntityModel<QuizSubmissionDTO> entityModel = EntityModel.of(quizSubmissionDTO,
                linkTo(methodOn(QuizSubmissionController.class).getQuizSubmissionById(quizSubmissionDTO.getId())).withSelfRel(),
                linkTo(methodOn(QuizSubmissionController.class).getAllQuizSubmissions(defaultPageable)).withRel("all-submissions"),
                linkTo(methodOn(QuizSubmissionController.class).getQuizSubmissionsByQuizId(quizSubmissionDTO.getQuizId(), defaultPageable)).withRel("quiz-submissions"),
                linkTo(methodOn(QuizSubmissionController.class).getQuizSubmissionsByStudentId(quizSubmissionDTO.getStudentId(), defaultPageable)).withRel("student-submissions"),
                linkTo(methodOn(QuizDetailsController.class).getQuizDetailsById(quizSubmissionDTO.getQuizId())).withRel("quiz-details"),
                linkTo(methodOn(StudentController.class).one(quizSubmissionDTO.getStudentId())).withRel("student")
        );

        logger.debug("Added links to EntityModel for QuizSubmissionDTO with ID: {}", quizSubmissionDTO.getId());
        logger.info("Completed assembling EntityModel for QuizSubmissionDTO with ID: {}", quizSubmissionDTO.getId());

        return entityModel;
    }
}
