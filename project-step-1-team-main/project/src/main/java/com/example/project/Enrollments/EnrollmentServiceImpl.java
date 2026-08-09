package com.example.project.Enrollments;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.CourseContents.CourseContent;
import com.example.project.CourseContents.CourseContentRepository;
import com.example.project.Courses.Course;
import com.example.project.Courses.CourseRepository;
import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Progress.Progress;
import com.example.project.Progress.ProgressRepository;
import com.example.project.Sections.Section;
import com.example.project.Sections.SectionRepository;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;
    private final ProgressRepository progressRepository;  
    private final EnrollmentModelAssembler assembler;
    private final EnrollmentMapper enrollmentMapper;
    private final CourseContentRepository courseContentRepository;


     public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            SectionRepository sectionRepository,
            ProgressRepository progressRepository,
            EnrollmentModelAssembler assembler,
            EnrollmentMapper enrollmentMapper,
            CourseContentRepository courseContentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.sectionRepository = sectionRepository;
        this.progressRepository = progressRepository;
        this.assembler = assembler;
        this.enrollmentMapper = enrollmentMapper;  
        this.courseContentRepository = courseContentRepository;

    }

    @Override
    public CollectionModel<EntityModel<EnrollmentDTO>> all(Pageable pageable) {
        logger.info("Entering method: all with pageable: {}", pageable);
        Page<Enrollment> enrollments = enrollmentRepository.findAll(pageable);
        List<EntityModel<EnrollmentDTO>> enrollmentDTOs = enrollments.getContent().stream()
                .map(enrollmentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        logger.info("Exiting method: all");
        return CollectionModel.of(enrollmentDTOs, linkTo(methodOn(EnrollmentController.class).all(pageable)).withSelfRel());
    }

    @Override
    public EntityModel<EnrollmentDTO> one(Long id) {
        logger.info("Entering method: one with ID: {}", id);
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment with ID " + id + " not found."));
        logger.info("Exiting method: one with ID: {}", id);
        return assembler.toModel(enrollmentMapper.toDTO(enrollment));
    }

    @Override
    public ResponseEntity<?> enrollStudent(EnrollmentDTO dto) {
        logger.info("Entering method: enrollStudent with DTO: {}", dto);
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student with ID " + dto.getStudentId()  + " not found."));

        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section with ID " + dto.getSectionId() + " not found."));

        if (enrollmentRepository.existsByStudentAndSection(student, section)) {
            throw new AlreadyExistsException(
                "Student " + dto.getStudentId() + " is already enrolled in section " + dto.getSectionId()
            );
        }

        Enrollment enrollment = new Enrollment(student, section);
        enrollmentRepository.save(enrollment);
        logger.info("Exiting method: enrollStudent with enrollment ID: {}", enrollment.getId());

        return ResponseEntity
            .created(linkTo(methodOn(EnrollmentController.class).one(enrollment.getId())).toUri())
            .body(enrollmentMapper.toDTO(enrollment));
            }

    @Override
    public ResponseEntity<?> updateEnrollmentProgress(Long enrollmentId, Long courseContentId) {
        logger.info("Entering method: updateEnrollmentProgress with enrollmentId: {}, courseContentId: {}", enrollmentId, courseContentId);
       
    Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Enrollment with ID " + enrollmentId + " not found."));
    
    // Check if this module (courseContent) has already been completed for this enrollment
    boolean alreadyCompleted = enrollment.getProgressRecords().stream()
            .filter(p -> p.getCourseContent() != null)
            .anyMatch(p -> p.getCourseContent().getId().equals(courseContentId));
    
    if (alreadyCompleted) {
        return ResponseEntity.badRequest().body("This module has already been completed.");
    }
    
    CourseContent completedModule = courseContentRepository.findById(courseContentId)
            .orElseThrow(() -> new ResourceNotFoundException("CourseContent with ID " + courseContentId + " not found."));
    
    Progress progress = new Progress(enrollment, completedModule, 0, "Module completed");
    progress.setUpdatedAt(LocalDateTime.now());
    progressRepository.save(progress);
        enrollment.getProgressRecords().add(progress);
    
   
    int totalModules = courseContentRepository.findByCourseId(enrollment.getCourse().getId()).size();
        long completedModules = enrollment.getProgressRecords().stream()
            .filter(p -> p.getCourseContent() != null)
            .map(p -> p.getCourseContent().getId())
            .distinct()
            .count();
    
    // Calculate new progress percentage (e.g., if 2 out of 4 modules are completed, progress = 50)
    double newProgress = ((double) completedModules / totalModules) * 100;
    if (newProgress > 100) {
        newProgress = 100;
    }
    
    enrollment.setCurrentProgress(newProgress);
    enrollmentRepository.save(enrollment);
    logger.info("Exiting method: updateEnrollmentProgress with updated progress: {}", newProgress);
    
    return ResponseEntity.ok(enrollmentMapper.toDTO(enrollment));
}
 
    @Override
    public ResponseEntity<?> removeEnrollment(Long id) {
        logger.info("Entering method: removeEnrollment with ID: {}", id);
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment with ID " + id + " not found.");
        }
        enrollmentRepository.deleteById(id);
        logger.info("Exiting method: removeEnrollment with ID: {}", id);
        return ResponseEntity.ok().build();
    }

    @Override
    public CollectionModel<EntityModel<EnrollmentDTO>> getEnrollmentsByStudent(Long studentId) {
        logger.info("Entering method: getEnrollmentsByStudent with studentId: {}", studentId);
        List<EntityModel<EnrollmentDTO>> enrollments = enrollmentRepository.findAllByStudentId(studentId).stream()
                .map(enrollmentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        logger.info("Exiting method: getEnrollmentsByStudent with studentId: {}", studentId);
        return CollectionModel.of(enrollments);
    }

    @Override
    public CollectionModel<EntityModel<EnrollmentDTO>> getEnrollmentsByCourse(Long courseId) {
        logger.info("Entering method: getEnrollmentsByCourse with courseId: {}", courseId);
List<EntityModel<EnrollmentDTO>> enrollments = enrollmentRepository.findAllBySectionCourseId(courseId).stream()                .map(enrollmentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        logger.info("Exiting method: getEnrollmentsByCourse with courseId: {}", courseId);
        return CollectionModel.of(enrollments);
    }

    @Override
public List<EnrollmentDTO> getPlainEnrollmentsByStudent(Long studentId) {
    return enrollmentRepository.findAllByStudentId(studentId).stream()
          .map(enrollmentMapper::toDTO)
          .collect(Collectors.toList());
}

}
