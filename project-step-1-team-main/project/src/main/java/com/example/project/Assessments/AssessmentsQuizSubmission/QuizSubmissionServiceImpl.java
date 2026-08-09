package com.example.project.Assessments.AssessmentsQuizSubmission;

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

import java.util.Map;

import com.example.project.Assessments.AssessmentsAssessmentGrade.AssessmentGrade;
import com.example.project.Assessments.AssessmentsAssessmentGrade.AssessmentGradeRepository;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetails;
import com.example.project.Assessments.AssessmentsQuizDetails.QuizDetailsRepository;
import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Assessments.Assessments_Assessment.AssessmentRepository;
import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QuizSubmissionServiceImpl implements QuizSubmissionService {

    private static final Logger logger = LoggerFactory.getLogger(QuizSubmissionServiceImpl.class);

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizDetailsRepository quizDetailsRepository;
    private final StudentRepository studentRepository;
    private final QuizSubmissionAssembler quizSubmissionAssembler;
    private final PagedResourcesAssembler<QuizSubmissionDTO> pagedAssembler;
private final AssessmentGradeRepository assessmentGradeRepository;
    private final AssessmentRepository assessmentRepository;

    public QuizSubmissionServiceImpl(
        QuizSubmissionRepository quizSubmissionRepository,
        QuizDetailsRepository quizDetailsRepository,
        StudentRepository studentRepository,
        QuizSubmissionAssembler quizSubmissionAssembler,
        PagedResourcesAssembler<QuizSubmissionDTO> pagedAssembler,
        AssessmentGradeRepository assessmentGradeRepository,
        AssessmentRepository assessmentRepository
    ) {
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.quizDetailsRepository = quizDetailsRepository;
        this.studentRepository = studentRepository;
        this.quizSubmissionAssembler = quizSubmissionAssembler;
        this.pagedAssembler = pagedAssembler;
        this.assessmentGradeRepository = assessmentGradeRepository;
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public PagedModel<EntityModel<QuizSubmissionDTO>> getAllQuizSubmissions(Pageable pageable) {
        logger.info("Fetching all quiz submissions with pageable: {}", pageable);
        Page<QuizSubmissionDTO> submissionsPage = quizSubmissionRepository.findAll(pageable)
                .map(QuizSubmissionMapper::toDTO);
        logger.debug("Fetched {} quiz submissions", submissionsPage.getContent().size());
        return pagedAssembler.toModel(submissionsPage, quizSubmissionAssembler);
    }

    @Override
    public EntityModel<QuizSubmissionDTO> getQuizSubmissionById(Long id) {
        logger.info("Fetching quiz submission by ID: {}", id);
        QuizSubmission quizSubmission = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz submission with ID {} not found", id);
                    return new ResourceNotFoundException("Quiz submission with ID " + id + " not found");
                });
        logger.debug("Fetched quiz submission: {}", quizSubmission);
        return quizSubmissionAssembler.toModel(QuizSubmissionMapper.toDTO(quizSubmission));
    }

    @Override
    public PagedModel<EntityModel<QuizSubmissionDTO>> getQuizSubmissionsByQuizId(Long quizId, Pageable pageable) {
        logger.info("Fetching quiz submissions for quiz ID: {}", quizId);
        Page<QuizSubmissionDTO> submissionsPage = quizSubmissionRepository.findByQuizId(quizId, pageable)
                .map(QuizSubmissionMapper::toDTO);

        if (submissionsPage.isEmpty()) {
            logger.warn("No submissions found for quiz ID {}", quizId);
            throw new ResourceNotFoundException("No submissions found for quiz ID " + quizId);
        }

        logger.debug("Fetched {} quiz submissions for quiz ID: {}", submissionsPage.getContent().size(), quizId);
        return pagedAssembler.toModel(submissionsPage, quizSubmissionAssembler);
    }

    @Override
    public PagedModel<EntityModel<QuizSubmissionDTO>> getQuizSubmissionsByStudentId(Long studentId, Pageable pageable) {
        logger.info("Fetching quiz submissions for student ID: {}", studentId);
        Page<QuizSubmissionDTO> submissionsPage = quizSubmissionRepository.findByStudentId(studentId, pageable)
                .map(QuizSubmissionMapper::toDTO);

        if (submissionsPage.isEmpty()) {
            logger.warn("No submissions found for student ID {}", studentId);
            throw new ResourceNotFoundException("No submissions found for student ID " + studentId);
        }

        logger.debug("Fetched {} quiz submissions for student ID: {}", submissionsPage.getContent().size(), studentId);
        return pagedAssembler.toModel(submissionsPage, quizSubmissionAssembler);
    }

   @Override
public ResponseEntity<?> createQuizSubmission(QuizSubmissionDTO dto) {
    logger.info("Creating a new quiz submission for quiz ID: {} and student ID: {}", dto.getQuizId(), dto.getStudentId());
    QuizDetails quizDetails = quizDetailsRepository.findById(dto.getQuizId())
            .orElseThrow(() -> {
                logger.warn("Quiz with ID {} not found", dto.getQuizId());
                return new ResourceNotFoundException("Quiz with ID " + dto.getQuizId() + " not found");
            });

    Student student = studentRepository.findById(dto.getStudentId())
            .orElseThrow(() -> {
                logger.warn("Student with ID {} not found", dto.getStudentId());
                return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
            });

    if (quizSubmissionRepository.findByQuizIdAndStudentId(dto.getQuizId(), dto.getStudentId()) != null) {
        logger.warn("A submission already exists for quiz ID {} and student ID {}", dto.getQuizId(), dto.getStudentId());
        throw new AlreadyExistsException("A submission already exists for quiz ID " + dto.getQuizId() + " and student ID " + dto.getStudentId());
    }

    QuizSubmission quizSubmission = QuizSubmissionMapper.toEntity(dto, quizDetails, student);
    quizSubmission = quizSubmissionRepository.save(quizSubmission);

    // Auto-grade and save the score
    double autoScore = gradeQuiz(quizDetails, quizSubmission.getAutoGradedAnswers());
    quizSubmission.setAutoGradedScore(autoScore);
    quizSubmissionRepository.save(quizSubmission); // Save the score!

    // SYNC TO ASSESSMENT_GRADE TABLE
    // 1. Find the Assessment for this quiz
    Assessment assessment = null;
    if (quizDetails.getAssessment() != null) {
        assessment = assessmentRepository.findById(quizDetails.getAssessment().getId()).orElse(null);
    }
    // 2. Find or create AssessmentGrade for this student and assessment
    if (assessment != null) {
        AssessmentGrade assessmentGrade = assessmentGradeRepository
            .findByAssessmentIdAndStudentId(assessment.getId(), student.getId())
            .orElse(new AssessmentGrade());

        assessmentGrade.setAssessment(assessment);
        assessmentGrade.setStudent(student);
        assessmentGrade.setQuizSubmission(quizSubmission);
        assessmentGrade.setAutoGradedScore(autoScore);
        assessmentGrade.setFinalScore(autoScore); // or null if you want to distinguish
        assessmentGrade.setFullyGraded(true); // or false if you want to require instructor review

        assessmentGradeRepository.save(assessmentGrade);
    }

    EntityModel<QuizSubmissionDTO> entityModel = quizSubmissionAssembler.toModel(QuizSubmissionMapper.toDTO(quizSubmission));
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
}


  @Override
public ResponseEntity<?> updateQuizSubmission(Long id, QuizSubmissionDTO dto) {
    logger.info("Updating quiz submission with ID: {}", id);
    QuizDetails quizDetails = quizDetailsRepository.findById(dto.getQuizId())
            .orElseThrow(() -> {
                logger.warn("Quiz with ID {} not found", dto.getQuizId());
                return new ResourceNotFoundException("Quiz with ID " + dto.getQuizId() + " not found");
            });

    Student student = studentRepository.findById(dto.getStudentId())
            .orElseThrow(() -> {
                logger.warn("Student with ID {} not found", dto.getStudentId());
                return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
            });

    return quizSubmissionRepository.findById(id)
            .map(quizSubmission -> {
                logger.debug("Found quiz submission with ID: {}", id);
                quizSubmission.setSubmissionStatus(dto.getSubmissionStatus());
                quizSubmission.setStartedAt(dto.getStartedAt());
                quizSubmission.setSubmittedAt(dto.getSubmittedAt());
                quizSubmission.setDurationMinutes(dto.getDurationMinutes());
                quizSubmission.setAutoGradedAnswers(dto.getAutoGradedAnswers());
                quizSubmission.setManuallyGradedAnswers(dto.getManuallyGradedAnswers());
                quizSubmission.setQuiz(quizDetails);
                quizSubmission.setStudent(student);

                // Regrade updated answers
                double autoScore = gradeQuiz(quizDetails, quizSubmission.getAutoGradedAnswers());
                quizSubmission.setAutoGradedScore(autoScore);

                quizSubmissionRepository.save(quizSubmission);
                logger.debug("Updated quiz submission: {}", quizSubmission);

                // --- SYNC TO ASSESSMENT_GRADE TABLE ---
                Assessment assessment = null;
                if (quizDetails.getAssessment() != null) {
                    assessment = assessmentRepository.findById(quizDetails.getAssessment().getId()).orElse(null);
                }
                if (assessment != null) {
                    AssessmentGrade assessmentGrade = assessmentGradeRepository
                        .findByAssessmentIdAndStudentId(assessment.getId(), student.getId())
                        .orElse(new AssessmentGrade());

                    assessmentGrade.setAssessment(assessment);
                    assessmentGrade.setStudent(student);
                    assessmentGrade.setQuizSubmission(quizSubmission);
                    assessmentGrade.setAutoGradedScore(autoScore);
                    assessmentGrade.setFinalScore(autoScore); // or leave null if you want manual grading later
                    assessmentGrade.setFullyGraded(true); // or false if manual review needed
                    assessmentGradeRepository.save(assessmentGrade);
                }
                // --------------------------------------

                EntityModel<QuizSubmissionDTO> entityModel = quizSubmissionAssembler.toModel(QuizSubmissionMapper.toDTO(quizSubmission));
                return ResponseEntity.ok(entityModel);
            })
            .orElseGet(() -> {
                logger.info("Quiz submission with ID {} not found. Creating a new quiz submission.", id);
                QuizSubmission newQuizSubmission = QuizSubmissionMapper.toEntity(dto, quizDetails, student);

                // Auto-grade new submission
                double autoScore = gradeQuiz(quizDetails, newQuizSubmission.getAutoGradedAnswers());
                newQuizSubmission.setAutoGradedScore(autoScore);

                QuizSubmission savedQuizSubmission = quizSubmissionRepository.save(newQuizSubmission);

                // --- SYNC TO ASSESSMENT_GRADE TABLE ---
                Assessment assessment = null;
                if (quizDetails.getAssessment() != null) {
                    assessment = assessmentRepository.findById(quizDetails.getAssessment().getId()).orElse(null);
                }
                if (assessment != null) {
                    AssessmentGrade assessmentGrade = assessmentGradeRepository
                        .findByAssessmentIdAndStudentId(assessment.getId(), student.getId())
                        .orElse(new AssessmentGrade());

                    assessmentGrade.setAssessment(assessment);
                    assessmentGrade.setStudent(student);
                    assessmentGrade.setQuizSubmission(savedQuizSubmission);
                    assessmentGrade.setAutoGradedScore(autoScore);
                    assessmentGrade.setFinalScore(autoScore); // or leave null if you want manual grading later
                    assessmentGrade.setFullyGraded(true); // or false if manual review needed
                    assessmentGradeRepository.save(assessmentGrade);
                }
                // --------------------------------------

                EntityModel<QuizSubmissionDTO> entityModel = quizSubmissionAssembler.toModel(QuizSubmissionMapper.toDTO(savedQuizSubmission));
                return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
            });
}


    @Override
    public ResponseEntity<?> deleteQuizSubmission(Long id) {
        logger.info("Deleting quiz submission with ID: {}", id);
        QuizSubmission quizSubmission = quizSubmissionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Quiz submission with ID {} not found", id);
                    return new ResourceNotFoundException("Quiz submission with ID " + id + " not found");
                });

        quizSubmissionRepository.delete(quizSubmission);
        logger.debug("Deleted quiz submission with ID: {}", id);

        return ResponseEntity.noContent().build();
    }

private double gradeQuiz(QuizDetails quiz, String autoGradedAnswersJson) {
    try {
        ObjectMapper mapper = new ObjectMapper();
        // The JSON is like: { "1": 5, "2": 9, ... }
        Map<String, Integer> answers = mapper.readValue(autoGradedAnswersJson, Map.class);
        double score = 0.0;
        for (var question : quiz.getQuestions()) { // assuming getQuestions() returns List<QuizQuestion>
            Integer selectedOption = answers.get(String.valueOf(question.getId()));
            if (
                selectedOption != null &&
                question.getCorrectOptionId() != null &&
                selectedOption.longValue() == question.getCorrectOptionId().longValue()
            ) {
                score += question.getScore() != null ? question.getScore() : 1.0;
            }
        }
        return score;
    } catch (Exception e) {
        // log error, default to 0 score
        return 0.0;
    }
}

}
