package com.example.project.Assessments.AssessmentsQuizOption;

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

import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestion;
import com.example.project.Assessments.AssessmentsQuizQuestion.QuizQuestionRepository;
import com.example.project.Exceptions.ResourceNotFoundException;

@Service
public class QuizOptionServiceImpl implements QuizOptionService {

    private static final Logger logger = LoggerFactory.getLogger(QuizOptionServiceImpl.class);

    private final QuizOptionRepository quizOptionRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizOptionAssembler quizOptionAssembler;
    private final PagedResourcesAssembler<QuizOptionDTO> pagedAssembler;

    public QuizOptionServiceImpl(
            QuizOptionRepository quizOptionRepository,
            QuizQuestionRepository quizQuestionRepository,
            QuizOptionAssembler quizOptionAssembler,
            PagedResourcesAssembler<QuizOptionDTO> pagedAssembler) {
        this.quizOptionRepository = quizOptionRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizOptionAssembler = quizOptionAssembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public PagedModel<EntityModel<QuizOptionDTO>> getAllQuizOptions(Pageable pageable) {
        logger.info("Fetching all quiz options with pageable: {}", pageable);
        Page<QuizOptionDTO> quizOptionPage = quizOptionRepository.findAll(pageable)
                .map(QuizOptionMapper::toDTO);
        logger.debug("Fetched {} quiz options", quizOptionPage.getContent().size());
        return pagedAssembler.toModel(quizOptionPage, quizOptionAssembler);
    }

    @Override
    public EntityModel<QuizOptionDTO> getQuizOptionById(Long id) {
        logger.info("Fetching quiz option by ID: {}", id);
        QuizOption quizOption = quizOptionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz option with ID {} not found", id);
                    return new ResourceNotFoundException("Quiz option with ID " + id + " not found");
                });
        logger.debug("Fetched quiz option: {}", quizOption);
        return quizOptionAssembler.toModel(QuizOptionMapper.toDTO(quizOption));
    }

    @Override
    public PagedModel<EntityModel<QuizOptionDTO>> getQuizOptionsByQuestionId(Long questionId, Pageable pageable) {
        logger.info("Fetching quiz options for question ID: {}", questionId);
        Page<QuizOptionDTO> quizOptionPage = quizOptionRepository.findByQuestionId(questionId, pageable)
                .map(QuizOptionMapper::toDTO);

        if (quizOptionPage.isEmpty()) {
            logger.warn("No options found for question ID {}", questionId);
            throw new ResourceNotFoundException("No options found for question ID " + questionId);
        }

        logger.debug("Fetched {} quiz options for question ID: {}", quizOptionPage.getContent().size(), questionId);
        return pagedAssembler.toModel(quizOptionPage, quizOptionAssembler);
    }

    @Override
    public ResponseEntity<?> createQuizOption(QuizOptionDTO dto) {
        logger.info("Creating a new quiz option for question ID: {}", dto.getQuestionId());
        QuizQuestion quizQuestion = quizQuestionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> {
                    logger.warn("Quiz question with ID {} not found", dto.getQuestionId());
                    return new ResourceNotFoundException("Quiz question with ID " + dto.getQuestionId() + " not found");
                });

        QuizOption quizOption = QuizOptionMapper.toEntity(dto, quizQuestion);
        quizOption = quizOptionRepository.save(quizOption);

        logger.debug("Created quiz option: {}", quizOption);

        EntityModel<QuizOptionDTO> entityModel = quizOptionAssembler.toModel(QuizOptionMapper.toDTO(quizOption));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> updateQuizOption(Long id, QuizOptionDTO dto) {
        logger.info("Updating quiz option with ID: {}", id);
        QuizQuestion quizQuestion = quizQuestionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> {
                    logger.warn("Quiz question with ID {} not found", dto.getQuestionId());
                    return new ResourceNotFoundException("Quiz question with ID " + dto.getQuestionId() + " not found");
                });

        return quizOptionRepository.findById(id)
                .map(quizOption -> {
                    logger.debug("Found quiz option with ID: {}", id);
                    quizOption.setOptionText(dto.getOptionText());
                    quizOption.setCorrect(dto.isCorrect());
                    quizOption.setQuestion(quizQuestion);

                    quizOptionRepository.save(quizOption);
                    logger.debug("Updated quiz option: {}", quizOption);

                    EntityModel<QuizOptionDTO> entityModel = quizOptionAssembler.toModel(QuizOptionMapper.toDTO(quizOption));
                    return ResponseEntity.ok(entityModel);
                })
                .orElseGet(() -> {
                    logger.info("Quiz option with ID {} not found. Creating a new quiz option.", id);
                    QuizOption newQuizOption = QuizOptionMapper.toEntity(dto, quizQuestion);
                    QuizOption savedQuizOption = quizOptionRepository.save(newQuizOption);

                    logger.debug("Created new quiz option: {}", savedQuizOption);

                    EntityModel<QuizOptionDTO> entityModel = quizOptionAssembler.toModel(QuizOptionMapper.toDTO(savedQuizOption));
                    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
                });
    }

    @Override
    public ResponseEntity<?> deleteQuizOption(Long id) {
        logger.info("Deleting quiz option with ID: {}", id);
        QuizOption quizOption = quizOptionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz option with ID {} not found", id);
                    return new ResourceNotFoundException("Quiz option with ID " + id + " not found");
                });

        quizOptionRepository.delete(quizOption);
        logger.debug("Deleted quiz option with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
