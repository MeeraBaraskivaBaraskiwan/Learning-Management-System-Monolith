package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import java.util.Optional;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetailsRepository;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;
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

@Service
public class AssignmentSubmissionServiceImpl implements AssignmentSubmissionService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentSubmissionServiceImpl.class);

    private final AssignmentSubmissionRepository repository;
    private final AssignmentDetailsRepository assignmentRepository;
    private final StudentRepository studentRepository;
    private final AssignmentSubmissionAssembler assembler;
    private final PagedResourcesAssembler<AssignmentSubmissionDTO> pagedAssembler;

    public AssignmentSubmissionServiceImpl(
            AssignmentSubmissionRepository repository,
            AssignmentDetailsRepository assignmentRepository,
            StudentRepository studentRepository,
            AssignmentSubmissionAssembler assembler,
            PagedResourcesAssembler<AssignmentSubmissionDTO> pagedAssembler) {
        this.repository = repository;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public PagedModel<EntityModel<AssignmentSubmissionDTO>> getAllAssignmentSubmissions(Pageable pageable) {
        logger.info("Fetching all assignment submissions with pageable: {}", pageable);
        Page<AssignmentSubmissionDTO> page = repository.findAll(pageable)
                .map(AssignmentSubmissionMapper::toDTO);
        logger.debug("Fetched {} assignment submissions", page.getContent().size());
        return pagedAssembler.toModel(page, assembler);
    }

    @Override
    public EntityModel<AssignmentSubmissionDTO> getAssignmentSubmissionById(Long id) {
        logger.info("Fetching assignment submission by ID: {}", id);
        AssignmentSubmission submission = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Assignment submission with ID {} not found", id);
                    return new ResourceNotFoundException("Assignment submission with ID " + id + " not found");
                });
        logger.debug("Fetched assignment submission: {}", submission);
        return assembler.toModel(AssignmentSubmissionMapper.toDTO(submission));
    }

    @Override
    public PagedModel<EntityModel<AssignmentSubmissionDTO>> getSubmissionsByAssignmentId(Long assignmentId, Pageable pageable) {
        logger.info("Fetching submissions for assignment ID: {}", assignmentId);
        Page<AssignmentSubmissionDTO> page = repository.findByAssignment_Id(assignmentId, pageable)
                .map(AssignmentSubmissionMapper::toDTO);
        logger.debug("Fetched {} submissions for assignment ID: {}", page.getContent().size(), assignmentId);
        return pagedAssembler.toModel(page, assembler);
    }

    @Override
    public PagedModel<EntityModel<AssignmentSubmissionDTO>> getSubmissionsByStudentId(Long studentId, Pageable pageable) {
        logger.info("Fetching submissions for student ID: {}", studentId);
        Page<AssignmentSubmissionDTO> page = repository.findByStudent_Id(studentId, pageable)
                .map(AssignmentSubmissionMapper::toDTO);
        logger.debug("Fetched {} submissions for student ID: {}", page.getContent().size(), studentId);
        return pagedAssembler.toModel(page, assembler);
    }

    @Override
    public EntityModel<AssignmentSubmissionDTO> getSubmissionByAssignmentIdAndStudentId(Long assignmentId, Long studentId) {
        logger.info("Fetching submission for assignment ID: {} and student ID: {}", assignmentId, studentId);
        AssignmentSubmission submission = repository.findAll(Pageable.unpaged()).stream()
                .filter(s -> s.getAssignment().getId().equals(assignmentId) && s.getStudent().getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Submission not found for assignment ID {} and student ID {}", assignmentId, studentId);
                    return new ResourceNotFoundException("Submission not found for assignment ID " + assignmentId + " and student ID " + studentId);
                });
        logger.debug("Fetched submission: {}", submission);
        return assembler.toModel(AssignmentSubmissionMapper.toDTO(submission));
    }

    @Override
    public ResponseEntity<?> createAssignmentSubmission(AssignmentSubmissionDTO dto) {
        logger.info("Creating assignment submission for assignment ID: {} and student ID: {}", dto.getAssignmentId(), dto.getStudentId());
        AssignmentDetails assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> {
                    logger.warn("Assignment with ID {} not found", dto.getAssignmentId());
                    return new ResourceNotFoundException("Assignment with ID " + dto.getAssignmentId() + " not found");
                });

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> {
                    logger.warn("Student with ID {} not found", dto.getStudentId());
                    return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
                });

        AssignmentSubmission submission = AssignmentSubmissionMapper.toEntity(dto, assignment, student);
        submission = repository.save(submission);
        logger.debug("Created assignment submission: {}", submission);

        EntityModel<AssignmentSubmissionDTO> entityModel = assembler.toModel(AssignmentSubmissionMapper.toDTO(submission));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @Override
    public ResponseEntity<?> updateAssignmentSubmission(Long id, AssignmentSubmissionDTO dto) {
        logger.info("Updating assignment submission with ID: {}", id);
        AssignmentDetails assignment = assignmentRepository.findById(dto.getAssignmentId())
                .orElseThrow(() -> {
                    logger.warn("Assignment with ID {} not found", dto.getAssignmentId());
                    return new ResourceNotFoundException("Assignment with ID " + dto.getAssignmentId() + " not found");
                });

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> {
                    logger.warn("Student with ID {} not found", dto.getStudentId());
                    return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
                });

        return repository.findById(id)
                .map(submission -> {
                    logger.debug("Found existing submission with ID: {}", id);
                    submission.setAssignment(assignment);
                    submission.setStudent(student);
                    submission.setFeedback(dto.getFeedback());
                    repository.save(submission);
                    logger.debug("Updated assignment submission: {}", submission);

                    EntityModel<AssignmentSubmissionDTO> entityModel = assembler.toModel(AssignmentSubmissionMapper.toDTO(submission));
                    return ResponseEntity.ok(entityModel);
                })
                .orElseGet(() -> {
                    logger.info("Submission with ID {} not found. Creating new submission.", id);
                    AssignmentSubmission newSubmission = AssignmentSubmissionMapper.toEntity(dto, assignment, student);
                    AssignmentSubmission saved = repository.save(newSubmission);
                    logger.debug("Created new assignment submission: {}", saved);

                    EntityModel<AssignmentSubmissionDTO> entityModel = assembler.toModel(AssignmentSubmissionMapper.toDTO(saved));
                    return ResponseEntity
                            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                            .body(entityModel);
                });
    }

    @Override
    public ResponseEntity<?> deleteAssignmentSubmission(Long id) {
        logger.info("Deleting assignment submission with ID: {}", id);
        AssignmentSubmission submission = repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Assignment submission with ID {} not found", id);
                    return new ResourceNotFoundException("Assignment submission with ID " + id + " not found");
                });

        repository.delete(submission);
        logger.debug("Deleted assignment submission with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> addOrUpdateFeedback(Long submissionId, String feedback) {
        logger.info("Adding or updating feedback for submission ID: {}", submissionId);
        AssignmentSubmission submission = repository.findById(submissionId)
                .orElseThrow(() -> {
                    logger.warn("Assignment submission with ID {} not found", submissionId);
                    return new ResourceNotFoundException("Assignment submission with ID " + submissionId + " not found");
                });

        submission.setFeedback(feedback);
        repository.save(submission);
        logger.debug("Updated feedback for submission ID: {}", submissionId);

        EntityModel<AssignmentSubmissionDTO> entityModel = assembler.toModel(AssignmentSubmissionMapper.toDTO(submission));
        return ResponseEntity.ok(entityModel);
    }

    @Override
public Optional<AssignmentSubmissionDTO> findSubmissionByAssignmentAndStudent(
    Long assignmentId,
    Long studentId
) {
    logger.info("Looking up submission for assignment {} / student {}", assignmentId, studentId);
    return repository
        .findByAssignment_IdAndStudent_Id(assignmentId, studentId)
        .map(AssignmentSubmissionMapper::toDTO);
}

}