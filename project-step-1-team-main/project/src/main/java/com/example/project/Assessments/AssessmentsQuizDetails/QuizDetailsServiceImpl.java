package com.example.project.Assessments.AssessmentsQuizDetails;

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

import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Assessments.Assessments_Assessment.AssessmentRepository;
import com.example.project.Assessments.Assessments_Assessment.AssessmentType;
import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Exceptions.ValidationException;

@Service
public class QuizDetailsServiceImpl implements QuizDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(QuizDetailsServiceImpl.class);

    private final QuizDetailsRepository quizDetailsRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuizDetailsAssembler quizDetailsAssembler;
    private final PagedResourcesAssembler<QuizDetailsDTO> pagedAssembler;

    public QuizDetailsServiceImpl(
            QuizDetailsRepository quizDetailsRepository,
            AssessmentRepository assessmentRepository,
            QuizDetailsAssembler quizDetailsAssembler,
            PagedResourcesAssembler<QuizDetailsDTO> pagedAssembler) {
        this.quizDetailsRepository = quizDetailsRepository;
        this.assessmentRepository = assessmentRepository;
        this.quizDetailsAssembler = quizDetailsAssembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public PagedModel<EntityModel<QuizDetailsDTO>> getAllQuizDetails(Pageable pageable) {
        logger.info("Fetching all quiz details with pageable: {}", pageable);
        Page<QuizDetailsDTO> quizDetailsPage = quizDetailsRepository.findAll(pageable)
                .map(QuizDetailsMapper::toDTO);
        logger.debug("Fetched {} quiz details", quizDetailsPage.getContent().size());
        return pagedAssembler.toModel(quizDetailsPage, quizDetailsAssembler);
    }

    @Override
    public EntityModel<QuizDetailsDTO> getQuizDetailsById(Long id) {
        logger.info("Fetching quiz details by ID: {}", id);
        QuizDetails quizDetails = quizDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizDetails with ID " + id + " not found"));
        logger.debug("Fetched quiz details: {}", quizDetails);
        return quizDetailsAssembler.toModel(QuizDetailsMapper.toDTO(quizDetails));
    }

    @Override
    public EntityModel<QuizDetailsDTO> getQuizDetailsByAssessmentId(Long assessmentId) {
        logger.info("Fetching quiz details by assessment ID: {}", assessmentId);
        QuizDetails quizDetails = quizDetailsRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("No quiz details found for assessment ID " + assessmentId));
        logger.debug("Fetched quiz details for assessment ID {}: {}", assessmentId, quizDetails);
        return quizDetailsAssembler.toModel(QuizDetailsMapper.toDTO(quizDetails));
    }

    @Override
    public ResponseEntity<?> createQuizDetails(QuizDetailsDTO quizDetailsDTO) {
        logger.info("Creating quiz details for assessment ID: {}", quizDetailsDTO.getAssessmentId());
        if (quizDetailsRepository.existsByAssessmentId(quizDetailsDTO.getAssessmentId())) {
            logger.warn("Quiz details already exist for assessment ID: {}", quizDetailsDTO.getAssessmentId());
            throw new AlreadyExistsException("Quiz details already exist for this assessment.");
        }

        Assessment assessment = assessmentRepository.findById(quizDetailsDTO.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment with ID " + quizDetailsDTO.getAssessmentId() + " not found"));

        if (assessment.getType() != AssessmentType.QUIZ) {
            logger.warn("Cannot create quiz details for a non-QUIZ assessment. Assessment ID: {}", quizDetailsDTO.getAssessmentId());
            throw new ValidationException(
                    "Cannot create quiz details for a non-QUIZ assessment. " +
                            "Assessment ID: " + quizDetailsDTO.getAssessmentId()
            );
        }

        QuizDetails quizDetails = QuizDetailsMapper.toEntity(quizDetailsDTO, assessment);
        quizDetails = quizDetailsRepository.save(quizDetails);
        logger.debug("Created quiz details: {}", quizDetails);

        EntityModel<QuizDetailsDTO> entityModel = quizDetailsAssembler.toModel(QuizDetailsMapper.toDTO(quizDetails));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> updateQuizDetails(Long id, QuizDetailsDTO quizDetailsDTO) {
        logger.info("Updating quiz details with ID: {}", id);
        Assessment assessment = assessmentRepository.findById(quizDetailsDTO.getAssessmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Assessment with ID " + quizDetailsDTO.getAssessmentId() + " not found"));

        if (assessment.getType() != AssessmentType.QUIZ) {
            logger.warn("Cannot update quiz details for a non-QUIZ assessment. Assessment ID: {}", quizDetailsDTO.getAssessmentId());
            throw new ValidationException(
                    "Cannot update quiz details for a non-QUIZ assessment. " +
                            "Assessment ID: " + quizDetailsDTO.getAssessmentId()
            );
        }

        return quizDetailsRepository.findById(id)
                .map(quizDetails -> {
                    logger.debug("Found quiz details with ID: {}", id);
                    quizDetails.setOpenTime(quizDetailsDTO.getOpenTime());
                    quizDetails.setClosingTime(quizDetailsDTO.getClosingTime());
                    quizDetails.setTimeLimitMinutes(quizDetailsDTO.getTimeLimitMinutes());
                    quizDetails.setPublished(quizDetailsDTO.isPublished());
                    quizDetails.setTotalScore(quizDetailsDTO.getTotalScore());
                    quizDetails.setAssessment(assessment);

                    quizDetailsRepository.save(quizDetails);
                    logger.debug("Updated quiz details: {}", quizDetails);
                    EntityModel<QuizDetailsDTO> entityModel = quizDetailsAssembler.toModel(QuizDetailsMapper.toDTO(quizDetails));

                    return ResponseEntity.ok(entityModel);
                })
                .orElseGet(() -> {
                    logger.info("Quiz details with ID {} not found. Creating new quiz details.", id);
                    QuizDetails newQuizDetails = QuizDetailsMapper.toEntity(quizDetailsDTO, assessment);
                    QuizDetails savedQuizDetails = quizDetailsRepository.save(newQuizDetails);

                    logger.debug("Created new quiz details: {}", savedQuizDetails);
                    EntityModel<QuizDetailsDTO> entityModel = quizDetailsAssembler.toModel(QuizDetailsMapper.toDTO(savedQuizDetails));
                    return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
                });
    }

    @Override
    public ResponseEntity<?> deleteQuizDetails(Long id) {
        logger.info("Deleting quiz details with ID: {}", id);
        QuizDetails quizDetails = quizDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuizDetails with ID " + id + " not found"));

        Assessment assessment = quizDetails.getAssessment();
        if (assessment != null) {
            logger.debug("Unlinking quiz details from assessment with ID: {}", assessment.getId());
            assessment.setQuizDetails(null);
        }

        quizDetailsRepository.delete(quizDetails);
        logger.debug("Deleted quiz details with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
