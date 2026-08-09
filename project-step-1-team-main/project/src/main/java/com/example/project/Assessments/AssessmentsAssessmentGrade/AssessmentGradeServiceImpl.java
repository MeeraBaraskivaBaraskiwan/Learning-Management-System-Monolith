package com.example.project.Assessments.AssessmentsAssessmentGrade;

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

import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmissionRepository;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmission;
import com.example.project.Assessments.AssessmentsQuizSubmission.QuizSubmissionRepository;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Assessments.Assessments_Assessment.AssessmentRepository;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;

@Service
public class AssessmentGradeServiceImpl implements AssessmentGradeService {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentGradeServiceImpl.class);

    private final AssessmentGradeRepository gradeRepository;
    private final AssessmentRepository assessmentRepository;
    private final StudentRepository studentRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final AssessmentGradeAssembler gradeAssembler;
    private final PagedResourcesAssembler<AssessmentGradeDTO> pagedAssembler;

    public AssessmentGradeServiceImpl(
        AssessmentGradeRepository gradeRepository,
        AssessmentRepository assessmentRepository,
        StudentRepository studentRepository,
        QuizSubmissionRepository quizSubmissionRepository,
        AssignmentSubmissionRepository assignmentSubmissionRepository,
        AssessmentGradeAssembler gradeAssembler,
        PagedResourcesAssembler<AssessmentGradeDTO> pagedAssembler
    ) {
        this.gradeRepository = gradeRepository;
        this.assessmentRepository = assessmentRepository;
        this.studentRepository = studentRepository;
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
        this.gradeAssembler = gradeAssembler;
        this.pagedAssembler = pagedAssembler;
    }

    @Override
    public PagedModel<EntityModel<AssessmentGradeDTO>> getAllGrades(Pageable pageable) {
        logger.info("Fetching all grades with pageable: {}", pageable);
        Page<AssessmentGradeDTO> page = gradeRepository.findAll(pageable)
            .map(AssessmentGradeMapper::toDTO);
        logger.debug("Fetched {} grades", page.getContent().size());
        return pagedAssembler.toModel(page, gradeAssembler);
    }

    @Override
    public EntityModel<AssessmentGradeDTO> getGradeById(Long id) {
        logger.info("Fetching grade by ID: {}", id);
        AssessmentGrade grade = gradeRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Grade with ID {} not found", id);
                return new ResourceNotFoundException("Grade with ID " + id + " not found");
            });
        logger.debug("Fetched grade: {}", grade);
        return gradeAssembler.toModel(AssessmentGradeMapper.toDTO(grade));
    }

    @Override
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByAssessmentId(Long assessmentId, Pageable pageable) {
        logger.info("Fetching grades for assessment ID: {}", assessmentId);
        Page<AssessmentGradeDTO> page = gradeRepository.findByAssessmentId(assessmentId, pageable)
            .map(AssessmentGradeMapper::toDTO);
        logger.debug("Fetched {} grades for assessment ID: {}", page.getContent().size(), assessmentId);
        return pagedAssembler.toModel(page, gradeAssembler);
    }

    @Override
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByStudentId(Long studentId, Pageable pageable) {
        logger.info("Fetching grades for student ID: {}", studentId);
        Page<AssessmentGradeDTO> page = gradeRepository.findByStudentId(studentId, pageable)
            .map(AssessmentGradeMapper::toDTO);
        logger.debug("Fetched {} grades for student ID: {}", page.getContent().size(), studentId);
        return pagedAssembler.toModel(page, gradeAssembler);
    }

    @Override
    public EntityModel<AssessmentGradeDTO> getGradeByAssessmentAndStudent(Long assessmentId, Long studentId) {
        logger.info("Fetching grade for assessment ID: {} and student ID: {}", assessmentId, studentId);
        AssessmentGrade grade = gradeRepository.findByAssessmentIdAndStudentId(assessmentId, studentId)
            .orElseThrow(() -> {
                logger.warn("Grade not found for assessment ID {} and student ID {}", assessmentId, studentId);
                return new ResourceNotFoundException("Grade not found for assessment ID " + assessmentId + " and student ID " + studentId);
            });
        logger.debug("Fetched grade: {}", grade);
        return gradeAssembler.toModel(AssessmentGradeMapper.toDTO(grade));
    }

    @Override
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByStudentAndCourse(String courseCode, Long studentId, Pageable pageable) {
        logger.info("Fetching grades for student ID: {} and course code: {}", studentId, courseCode);
        Page<AssessmentGradeDTO> page = gradeRepository.findByStudentIdAndAssessment_Course_Code(studentId, courseCode, pageable)
            .map(AssessmentGradeMapper::toDTO);
        logger.debug("Fetched {} grades for student ID: {} and course code: {}", page.getContent().size(), studentId, courseCode);
        return pagedAssembler.toModel(page, gradeAssembler);
    }

    @Override
    public PagedModel<EntityModel<AssessmentGradeDTO>> getGradesByCourseAndSection(String courseCode, int sectionNumber, Pageable pageable) {
        logger.info("Fetching grades for course code: {} and section number: {}", courseCode, sectionNumber);
        Page<AssessmentGradeDTO> page = gradeRepository.findByAssessment_Course_CodeAndAssessment_SectionNumber(courseCode, sectionNumber, pageable)
            .map(AssessmentGradeMapper::toDTO);
        logger.debug("Fetched {} grades for course code: {} and section number: {}", page.getContent().size(), courseCode, sectionNumber);
        return pagedAssembler.toModel(page, gradeAssembler);
    }

    @Override
    public ResponseEntity<?> createGrade(AssessmentGradeDTO dto) {
        logger.info("Creating grade for assessment ID: {} and student ID: {}", dto.getAssessmentId(), dto.getStudentId());
        Assessment assessment = assessmentRepository.findById(dto.getAssessmentId())
            .orElseThrow(() -> {
                logger.warn("Assessment with ID {} not found", dto.getAssessmentId());
                return new ResourceNotFoundException("Assessment with ID " + dto.getAssessmentId() + " not found");
            });

        Student student = studentRepository.findById(dto.getStudentId())
            .orElseThrow(() -> {
                logger.warn("Student with ID {} not found", dto.getStudentId());
                return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
            });

        QuizSubmission quizSubmission = null;
        if (dto.getQuizSubmissionId() != null) {
            quizSubmission = quizSubmissionRepository.findById(dto.getQuizSubmissionId())
                .orElseThrow(() -> {
                    logger.warn("Quiz submission with ID {} not found", dto.getQuizSubmissionId());
                    return new ResourceNotFoundException("Quiz submission with ID " + dto.getQuizSubmissionId() + " not found");
                });
        }

        AssignmentSubmission assignmentSubmission = null;
        if (dto.getAssignmentSubmissionId() != null) {
            assignmentSubmission = assignmentSubmissionRepository.findById(dto.getAssignmentSubmissionId())
                .orElseThrow(() -> {
                    logger.warn("Assignment submission with ID {} not found", dto.getAssignmentSubmissionId());
                    return new ResourceNotFoundException("Assignment submission with ID " + dto.getAssignmentSubmissionId() + " not found");
                });
        }

        AssessmentGrade grade = AssessmentGradeMapper.toEntity(dto, assessment, student, quizSubmission, assignmentSubmission);
        grade = gradeRepository.save(grade);
        logger.debug("Created grade: {}", grade);

        EntityModel<AssessmentGradeDTO> model = gradeAssembler.toModel(AssessmentGradeMapper.toDTO(grade));
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
    }

    @Override
    public ResponseEntity<?> updateGrade(Long id, AssessmentGradeDTO dto) {
        logger.info("Updating grade with ID: {}", id);
        Assessment assessment = assessmentRepository.findById(dto.getAssessmentId())
            .orElseThrow(() -> {
                logger.warn("Assessment with ID {} not found", dto.getAssessmentId());
                return new ResourceNotFoundException("Assessment not found");
            });

        Student student = studentRepository.findById(dto.getStudentId())
            .orElseThrow(() -> {
                logger.warn("Student with ID {} not found", dto.getStudentId());
                return new ResourceNotFoundException("Student not found");
            });

        final QuizSubmission quizSubmission;
        if (dto.getQuizSubmissionId() != null) {
            quizSubmission = quizSubmissionRepository.findById(dto.getQuizSubmissionId())
                .orElseThrow(() -> {
                    logger.warn("Quiz submission with ID {} not found", dto.getQuizSubmissionId());
                    return new ResourceNotFoundException("Quiz submission not found");
                });
        } else {
            quizSubmission = null;
        }

        final AssignmentSubmission assignmentSubmission;
        if (dto.getAssignmentSubmissionId() != null) {
            assignmentSubmission = assignmentSubmissionRepository.findById(dto.getAssignmentSubmissionId())
                .orElseThrow(() -> {
                    logger.warn("Assignment submission with ID {} not found", dto.getAssignmentSubmissionId());
                    return new ResourceNotFoundException("Assignment submission not found");
                });
        } else {
            assignmentSubmission = null;
        }

        return gradeRepository.findById(id)
            .map(existing -> {
                logger.debug("Found existing grade with ID: {}", id);
                existing.setAutoGradedScore(dto.getAutoGradedScore());
                existing.setFinalScore(dto.getFinalScore());
                existing.setGradingComments(dto.getGradingComments());
                existing.setFullyGraded(dto.isFullyGraded());
                gradeRepository.save(existing);
                logger.debug("Updated grade: {}", existing);
                return ResponseEntity.ok(
                    gradeAssembler.toModel(AssessmentGradeMapper.toDTO(existing))
                );
            })
            .orElseGet(() -> {
                logger.info("Grade with ID {} not found. Creating new grade.", id);
                AssessmentGrade grade = AssessmentGradeMapper.toEntity(dto, assessment, student, quizSubmission, assignmentSubmission);
                grade.setId(id);
                gradeRepository.save(grade);
                logger.debug("Created new grade: {}", grade);
                EntityModel<AssessmentGradeDTO> model = gradeAssembler.toModel(AssessmentGradeMapper.toDTO(grade));
                return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(model);
            });
    }

    @Override
    public ResponseEntity<?> deleteGrade(Long id) {
        logger.info("Deleting grade with ID: {}", id);
        if (!gradeRepository.existsById(id)) {
            logger.warn("Grade with ID {} not found", id);
            throw new ResourceNotFoundException("Grade with ID " + id + " not found");
        }
        gradeRepository.deleteById(id);
        logger.debug("Deleted grade with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
