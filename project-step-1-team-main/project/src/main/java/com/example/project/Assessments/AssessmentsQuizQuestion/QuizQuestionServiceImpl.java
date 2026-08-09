package com.example.project.Assessments.AssessmentsQuizQuestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetailsRepository;
import com.example.project.Exceptions.ResourceNotFoundException;

@Service
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private static final Logger logger = LoggerFactory.getLogger(QuizQuestionServiceImpl.class);

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizDetailsRepository quizDetailsRepository;
    private final QuizQuestionAssembler quizQuestionAssembler;
    private final PagedResourcesAssembler<QuizQuestionDTO> pagedAssembler;

    public QuizQuestionServiceImpl(
            QuizQuestionRepository quizQuestionRepository,
            QuizDetailsRepository quizDetailsRepository,
            QuizQuestionAssembler quizQuestionAssembler,
            PagedResourcesAssembler<QuizQuestionDTO> pagedAssembler) {
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizDetailsRepository = quizDetailsRepository;
        this.quizQuestionAssembler = quizQuestionAssembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public PagedModel<EntityModel<QuizQuestionDTO>> getAllQuizQuestions(Pageable pageable) {
        logger.info("Fetching all quiz questions with pageable: {}", pageable);
        Page<QuizQuestionDTO> quizQuestionPage = quizQuestionRepository.findAll(pageable)
                .map(QuizQuestionMapper::toDTO);
        logger.debug("Fetched {} quiz questions", quizQuestionPage.getContent().size());
        return pagedAssembler.toModel(quizQuestionPage, quizQuestionAssembler);
    }

    @Override
    public EntityModel<QuizQuestionDTO> getQuizQuestionById(Long id) {
        logger.info("Fetching quiz question by ID: {}", id);
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz question with ID {} not found", id);
                    return new ResourceNotFoundException("Quiz question with ID " + id + " not found");
                });
        logger.debug("Fetched quiz question: {}", quizQuestion);
        return quizQuestionAssembler.toModel(QuizQuestionMapper.toDTO(quizQuestion));
    }

    @Override
    public PagedModel<EntityModel<QuizQuestionDTO>> getQuizQuestionsByQuizId(Long quizId, Pageable pageable) {
        logger.info("Fetching quiz questions for quiz ID: {}", quizId);
        Page<QuizQuestionDTO> quizQuestionPage = quizQuestionRepository.findByQuizId(quizId, pageable)
                .map(QuizQuestionMapper::toDTO);

        if (quizQuestionPage.isEmpty()) {
            logger.warn("No questions found for quiz ID {}", quizId);
            throw new ResourceNotFoundException("No questions found for quiz ID " + quizId);
        }

        logger.debug("Fetched {} quiz questions for quiz ID: {}", quizQuestionPage.getContent().size(), quizId);
        return pagedAssembler.toModel(quizQuestionPage, quizQuestionAssembler);
    }

   @Override
public ResponseEntity<?> createQuizQuestion(QuizQuestionDTO dto) {
    logger.info("Creating a new quiz question for quiz ID: {}", dto.getQuizId());
    QuizDetails quizDetails = quizDetailsRepository.findById(dto.getQuizId())
            .orElseThrow(() -> {
                logger.warn("Quiz with ID {} not found", dto.getQuizId());
                return new ResourceNotFoundException("Quiz with ID " + dto.getQuizId() + " not found");
            });

    int nextQuestionNumber = quizQuestionRepository.findByQuizId(dto.getQuizId(), Pageable.unpaged()).getNumberOfElements() + 1;
    dto.setQuestionNumber(nextQuestionNumber);

    QuizQuestion quizQuestion = QuizQuestionMapper.toEntity(dto, quizDetails);

    // --- Make sure to set correctOptionId here too! ---
    quizQuestion.setCorrectOptionId(dto.getCorrectOptionId());

    quizQuestion = quizQuestionRepository.save(quizQuestion);

    logger.debug("Created quiz question: {}", quizQuestion);

    EntityModel<QuizQuestionDTO> entityModel = quizQuestionAssembler.toModel(QuizQuestionMapper.toDTO(quizQuestion));
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
}


    @Override
public ResponseEntity<?> updateQuizQuestion(Long id, QuizQuestionDTO dto) {
    logger.info("Updating quiz question with ID: {}", id);
    QuizDetails quizDetails = quizDetailsRepository.findById(dto.getQuizId())
            .orElseThrow(() -> {
                logger.warn("Quiz with ID {} not found", dto.getQuizId());
                return new ResourceNotFoundException("Quiz with ID " + dto.getQuizId() + " not found");
            });

    return quizQuestionRepository.findById(id)
            .map(quizQuestion -> {
                logger.debug("Found quiz question with ID: {}", id);
                quizQuestion.setScore(dto.getScore());
                quizQuestion.setQuestionText(dto.getQuestionText());
                quizQuestion.setQuestionType(dto.getQuestionType());
                quizQuestion.setAutoGraded(dto.isAutoGraded());
                quizQuestion.setQuiz(quizDetails);
                quizQuestion.setCorrectOptionId(dto.getCorrectOptionId()); // <-- ADD THIS LINE

                quizQuestionRepository.save(quizQuestion);
                logger.debug("Updated quiz question: {}", quizQuestion);

                EntityModel<QuizQuestionDTO> entityModel = quizQuestionAssembler.toModel(QuizQuestionMapper.toDTO(quizQuestion));
                return ResponseEntity.ok(entityModel);
            })
            .orElseGet(() -> {
                logger.info("Quiz question with ID {} not found. Creating a new quiz question.", id);
                int nextQuestionNumber = quizQuestionRepository.findByQuizId(dto.getQuizId(), Pageable.unpaged()).getNumberOfElements() + 1;
                dto.setQuestionNumber(nextQuestionNumber);
                QuizQuestion newQuizQuestion = QuizQuestionMapper.toEntity(dto, quizDetails);
                QuizQuestion savedQuizQuestion = quizQuestionRepository.save(newQuizQuestion);

                logger.debug("Created new quiz question: {}", savedQuizQuestion);

                EntityModel<QuizQuestionDTO> entityModel = quizQuestionAssembler.toModel(QuizQuestionMapper.toDTO(savedQuizQuestion));
                return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
            });
}


    @Override
    public ResponseEntity<?> deleteQuizQuestion(Long id) {
        logger.info("Deleting quiz question with ID: {}", id);
        QuizQuestion quizQuestion = quizQuestionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz question with ID {} not found", id);
                    return new ResourceNotFoundException("Quiz question with ID " + id + " not found");
                });

        quizQuestionRepository.delete(quizQuestion);
        logger.debug("Deleted quiz question with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
