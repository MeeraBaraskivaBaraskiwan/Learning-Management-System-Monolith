package com.example.project.Progress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Enrollments.Enrollment;
import com.example.project.Enrollments.EnrollmentMapper;
import com.example.project.Enrollments.EnrollmentRepository;
import com.example.project.CourseContents.CourseContent;
import com.example.project.CourseContents.CourseContentRepository; 
import com.example.project.Exceptions.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProgressServiceImpl implements ProgressService {

    private static final Logger logger = LoggerFactory.getLogger(ProgressServiceImpl.class);

    private final ProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ProgressModelAssembler assembler;
    private final ProgressMapper progressMapper;
    private final EnrollmentMapper enrollmentMapper;
    private final CourseContentRepository courseContentRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository, EnrollmentRepository enrollmentRepository,
                               ProgressModelAssembler assembler, ProgressMapper progressMapper, EnrollmentMapper enrollmentMapper, CourseContentRepository courseContentRepository) {
        this.progressRepository = progressRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.assembler = assembler;
        this.progressMapper = progressMapper;
        this.enrollmentMapper = enrollmentMapper;
        this.courseContentRepository = courseContentRepository;
    }

    @Override
    public CollectionModel<EntityModel<ProgressDTO>> getProgressByEnrollment(Long enrollmentId) {
        logger.info("Fetching progress for enrollment ID: {}", enrollmentId);
        List<EntityModel<ProgressDTO>> progressRecords = progressRepository.findByEnrollmentId(enrollmentId)
                .stream()
                .map(progressMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(progressRecords, linkTo(methodOn(ProgressController.class)
                .getProgressByEnrollment(enrollmentId)).withSelfRel());
    }

    @Override
    public ResponseEntity<?> addProgress(ProgressDTO progressDTO) {
        logger.info("Adding progress for enrollment ID: {}", progressDTO.getEnrollmentId());
        Enrollment enrollment = enrollmentRepository.findById(progressDTO.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment with ID " + progressDTO.getEnrollmentId() + " not found."));

        CourseContent courseContent = null;
        if (progressDTO.getCourseContentId() != null) {
            courseContent = courseContentRepository.findById(progressDTO.getCourseContentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "CourseContent with ID " + progressDTO.getCourseContentId() + " not found."
                ));
        }

        double progressValue = calculateProgress(progressDTO.getCompletedTasks(), progressDTO.getTotalTasks());
        Progress progress;
        if (courseContent != null) {
            progress = new Progress(enrollment, courseContent, progressValue, "Progress updated");
        } else {
            progress = new Progress(enrollment, progressValue, "Progress updated");
        }
        progress.setUpdatedAt(LocalDateTime.now());

        progressRepository.save(progress);

        return ResponseEntity.ok(progressMapper.toDTO(progress));
    }

    @Override
    public ResponseEntity<?> deleteProgress(Long progressId) {
        logger.info("Deleting progress with ID: {}", progressId);
        return progressRepository.findById(progressId)
            .map(progress -> {
                progressRepository.delete(progress);
                return ResponseEntity.ok().body("Progress record deleted successfully.");
            })
            .orElseThrow(() -> new ResourceNotFoundException("Progress with ID " + progressId + " not found."));
    }

    @Override
    public ResponseEntity<?> updateProgress(Long enrollmentId, double progressValue, Long courseContentId) {
        logger.info("Updating progress for enrollment ID: {}, course content ID: {}", enrollmentId, courseContentId);

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment with ID " + enrollmentId + " not found."));

        CourseContent courseContent = null;
        if (courseContentId != null) {
            courseContent = courseContentRepository.findById(courseContentId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseContent with ID " + courseContentId + " not found."));
        }

        double calculatedProgress = calculateProgress(progressValue, 100); 
        Progress progress;
        if (courseContent != null) {
            progress = new Progress(enrollment, courseContent, calculatedProgress, "Progress updated");
        } else {
            progress = new Progress(enrollment, calculatedProgress, "Progress updated");
        }

        progress.setUpdatedAt(LocalDateTime.now());
        progressRepository.save(progress);

        enrollment.getProgressRecords().add(progress);
        enrollmentRepository.save(enrollment);

        return ResponseEntity.ok(enrollmentMapper.toDTO(enrollment));
    }

    private double calculateProgress(double progressValue, double totalTasks) {
        if (totalTasks == 0) {
            throw new IllegalArgumentException("Total tasks cannot be zero.");
        }
        return (double) progressValue / totalTasks * 100;
    }


    @Override
public CollectionModel<EntityModel<ProgressDTO>> getAllProgress() {
    List<EntityModel<ProgressDTO>> progressRecords = progressRepository.findAll()
            .stream()
            .map(progressMapper::toDTO)
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(progressRecords,
            linkTo(methodOn(ProgressController.class).getAllProgress()).withSelfRel());
}

}
