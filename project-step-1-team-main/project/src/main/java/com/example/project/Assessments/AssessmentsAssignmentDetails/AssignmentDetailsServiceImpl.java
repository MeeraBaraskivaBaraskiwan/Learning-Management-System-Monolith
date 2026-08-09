package com.example.project.Assessments.AssessmentsAssignmentDetails;

import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Assessments.Assessments_Assessment.AssessmentDTO;
import com.example.project.Assessments.Assessments_Assessment.AssessmentMapper;
import com.example.project.Assessments.Assessments_Assessment.AssessmentRepository;
import com.example.project.Assessments.Assessments_Assessment.AssessmentType;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Exceptions.ValidationException;

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

import reactor.core.publisher.Sinks;

@Service
public class AssignmentDetailsServiceImpl implements AssignmentDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentDetailsServiceImpl.class);

    private final AssignmentDetailsRepository assignmentDetailsRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssignmentDetailsAssembler assignmentDetailsAssembler;
    private final PagedResourcesAssembler<AssignmentDetailsDTO> pagedAssembler;
    private final Sinks.Many<AssessmentDTO> assessmentSink;

    public AssignmentDetailsServiceImpl(
            AssignmentDetailsRepository assignmentDetailsRepository,
            AssessmentRepository assessmentRepository,
            AssignmentDetailsAssembler assignmentDetailsAssembler,
            PagedResourcesAssembler<AssignmentDetailsDTO> pagedAssembler,Sinks.Many<AssessmentDTO> assessmentSink
    ) {
        this.assignmentDetailsRepository = assignmentDetailsRepository;
        this.assessmentRepository = assessmentRepository;
        this.assignmentDetailsAssembler = assignmentDetailsAssembler;
        this.pagedAssembler = pagedAssembler;
        this.assessmentSink    = assessmentSink;
    }

    @Override
    public PagedModel<EntityModel<AssignmentDetailsDTO>> getAll(Pageable pageable) {
        logger.info("Fetching all assignment details with pageable: {}", pageable);
        Page<AssignmentDetailsDTO> assignmentDetailsPage = assignmentDetailsRepository.findAll(pageable)
                .map(AssignmentDetailsMapper::toDTO);

        logger.debug("Fetched {} assignment details", assignmentDetailsPage.getContent().size());
        return pagedAssembler.toModel(assignmentDetailsPage, assignmentDetailsAssembler);
    }

    @Override
    public EntityModel<AssignmentDetailsDTO> getById(Long id) {
        logger.info("Fetching assignment details by ID: {}", id);
        AssignmentDetails assignmentDetails = assignmentDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("AssignmentDetails with ID {} not found", id);
                    return new ResourceNotFoundException("AssignmentDetails with ID " + id + " not found");
                });

        logger.debug("Fetched assignment details: {}", assignmentDetails);
        return assignmentDetailsAssembler.toModel(AssignmentDetailsMapper.toDTO(assignmentDetails));
    }

    @Override
    public EntityModel<AssignmentDetailsDTO> getByAssessmentId(Long assessmentId) {
        logger.info("Fetching assignment details by assessment ID: {}", assessmentId);
        AssignmentDetails assignmentDetails = assignmentDetailsRepository.findByAssessmentId(assessmentId)
                .orElseThrow(() -> {
                    logger.warn("AssignmentDetails with Assessment ID {} not found", assessmentId);
                    return new ResourceNotFoundException("AssignmentDetails with Assessment ID " + assessmentId + " not found");
                });

        logger.debug("Fetched assignment details: {}", assignmentDetails);
        return assignmentDetailsAssembler.toModel(AssignmentDetailsMapper.toDTO(assignmentDetails));
    }

    @Override
    public ResponseEntity<?> create(AssignmentDetailsDTO dto) {
        logger.info("Creating assignment details for assessment ID: {}", dto.getAssessmentId());
        Assessment assessment = assessmentRepository.findById(dto.getAssessmentId())
                .orElseThrow(() -> {
                    logger.warn("Assessment with ID {} not found", dto.getAssessmentId());
                    return new ResourceNotFoundException("Assessment with ID " + dto.getAssessmentId() + " not found");
                });

        if (assessment.getType() != AssessmentType.ASSIGNMENT) {
            logger.error("Cannot create assignment details for a non-ASSIGNMENT assessment. Assessment ID: {}", dto.getAssessmentId());
            throw new ValidationException(
                    "Cannot create assignment details for a non-ASSIGNMENT assessment. " +
                            "Assessment ID: " + dto.getAssessmentId()
            );
        }

        AssignmentDetails assignmentDetails = AssignmentDetailsMapper.toEntity(dto, assessment);
        assignmentDetails = assignmentDetailsRepository.save(assignmentDetails);

        logger.debug("Created assignment details: {}", assignmentDetails);

        EntityModel<AssignmentDetailsDTO> entityModel =
                assignmentDetailsAssembler.toModel(AssignmentDetailsMapper.toDTO(assignmentDetails));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> update(Long id, AssignmentDetailsDTO dto) {
        logger.info("Updating assignment details with ID: {}", id);
        Assessment assessment = assessmentRepository.findById(dto.getAssessmentId())
                .orElseThrow(() -> {
                    logger.warn("Assessment with ID {} not found", dto.getAssessmentId());
                    return new ResourceNotFoundException("Assessment with ID " + dto.getAssessmentId() + " not found");
                });

        if (assessment.getType() != AssessmentType.ASSIGNMENT) {
            logger.error("Cannot update assignment details for a non-ASSIGNMENT assessment. Assessment ID: {}", dto.getAssessmentId());
            throw new ValidationException(
                    "Cannot update assignment details for a non-ASSIGNMENT assessment. " +
                            "Assessment ID: " + dto.getAssessmentId()
            );
        }

        return assignmentDetailsRepository.findById(id).map(existing -> {
            logger.debug("Found existing assignment details with ID: {}", id);
            existing.setAssessment(assessment);
            existing.setTotalScore(dto.getTotalScore());
            existing.setNotes(dto.getNotes());
            existing.setPublished(dto.isPublished());

            assignmentDetailsRepository.save(existing);
            logger.debug("Updated assignment details: {}", existing);

            EntityModel<AssignmentDetailsDTO> entityModel = assignmentDetailsAssembler.toModel(AssignmentDetailsMapper.toDTO(existing));
            return ResponseEntity.ok(entityModel);

        }).orElseGet(() -> {
            logger.info("Assignment details with ID {} not found. Creating new assignment details.", id);
            AssignmentDetails newAssignmentDetails = AssignmentDetailsMapper.toEntity(dto, assessment);
            AssignmentDetails saved = assignmentDetailsRepository.save(newAssignmentDetails);

            logger.debug("Created new assignment details: {}", saved);

            EntityModel<AssignmentDetailsDTO> entityModel =
                    assignmentDetailsAssembler.toModel(AssignmentDetailsMapper.toDTO(saved));
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        });
    }

    @Override
    public ResponseEntity<?> publish(Long id) {
        logger.info("Publishing assignment details with ID: {}", id);
        AssignmentDetails assignmentDetails = assignmentDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("AssignmentDetails with ID {} not found", id);
                    return new ResourceNotFoundException("AssignmentDetails with ID " + id + " not found");
                });

        assignmentDetails.setPublished(true);
        assignmentDetailsRepository.save(assignmentDetails);

         AssessmentDTO dto = AssessmentMapper.toDTO(assignmentDetails.getAssessment());
          assessmentSink.tryEmitNext(dto);


        logger.debug("Published assignment details: {}", assignmentDetails);

        EntityModel<AssignmentDetailsDTO> entityModel =
                assignmentDetailsAssembler.toModel(AssignmentDetailsMapper.toDTO(assignmentDetails));
        return ResponseEntity.ok(entityModel);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        logger.info("Deleting assignment details with ID: {}", id);
        AssignmentDetails details = assignmentDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("AssignmentDetails with ID {} not found", id);
                    return new ResourceNotFoundException("AssignmentDetails with ID " + id + " not found");
                });

        Assessment assessment = details.getAssessment();
        if (assessment != null) {
            logger.debug("Unlinking assignment details from assessment with ID: {}", assessment.getId());
            assessment.setAssignmentDetails(null);
        }

        assignmentDetailsRepository.delete(details);
        logger.debug("Deleted assignment details with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}
