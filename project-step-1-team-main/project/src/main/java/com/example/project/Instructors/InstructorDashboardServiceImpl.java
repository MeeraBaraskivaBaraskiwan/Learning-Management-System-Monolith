package com.example.project.Instructors;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.project.Instructors.DashboardStatsDTO;
import com.example.project.Students.Student;
import com.example.project.Students.StudentDTO;
import com.example.project.Students.StudentMapper;
import com.example.project.Users.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.Courses.CourseRepository;
import com.example.project.Assessments.AssessmentsAssessmentGrade.AssessmentGradeRepository;
import com.example.project.CourseContents.CourseContentDTO;
import com.example.project.CourseContents.CourseContentService;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.DirectoryUserDTO;
import com.example.project.Users.UserRepository;   
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.example.project.Files.FileMetadataService;





@Service
public class InstructorDashboardServiceImpl implements InstructorDashboardService {



      @PersistenceContext
    private EntityManager em;

    private final StudentMapper          studentMapper;
    private final InstructorRepository   repo;
    private final CourseRepository       courseRepo;
    private final AssessmentGradeRepository gradeRepo;
    private final CourseContentService   contentService;
    private final UserRepository         userRepo;
    private final FileMetadataService    fileMetadataService;

    public InstructorDashboardServiceImpl(
            InstructorRepository repo,
            CourseRepository courseRepo,
            AssessmentGradeRepository gradeRepo,
            StudentMapper studentMapper,
            UserRepository userRepo,
            CourseContentService contentService,
            FileMetadataService fileMetadataService
    ) {
        this.repo                = repo;
        this.courseRepo          = courseRepo;
        this.gradeRepo           = gradeRepo;
        this.studentMapper       = studentMapper;
        this.userRepo            = userRepo;
        this.contentService      = contentService;
        this.fileMetadataService = fileMetadataService;
    }

    @Override
public List<CourseContentDTO> getContentForInstructor(Long id) {
  return contentService
    .getContentByInstructor(id)                // returns a CollectionModel<EntityModel<DTO>>
    .getContent()                              // get the Collection<EntityModel<DTO>>
    .stream()
    .map(EntityModel::getContent)              // unwrap DTO from each EntityModel
    .toList();
}


      @Override
    public DashboardStatsDTO getDashboard(Long id) {
        Instructor inst = repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Instructor " + id + " not found"));

        long totalCourses     = courseRepo.countByInstructorId(id);
        long totalStudents    = repo.countStudentsForInstructor(id);
        long totalAssessments = repo.countAssessmentsForInstructor(id);
        double passedRate     = repo.calcPassRate(id);   // %
        double impact         = repo.calcImpact(id);     // avg grade or your metric

        /* Pie chart: number of students per course */
        List<DashboardStatsDTO.PieSlice> report = courseRepo.findByInstructorId(id)
            .stream()
            .map(c -> new DashboardStatsDTO.PieSlice(c.getCode(), (long)c.getEnrollments().size()))
            .toList();

        /* Recent submissions – newest 10 */
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<DashboardStatsDTO.SubmissionRow> recent = gradeRepo
            .findTop10ByAssessment_InstructorIdOrderByCreatedAtDesc(id)
            .stream()
            .map(g -> {
        double finalScore = g.getFinalScore() != null ? g.getFinalScore() : 0.0;
        boolean passed = finalScore >= 50.0;  // your chosen pass threshold
        return new DashboardStatsDTO.SubmissionRow(
            g.getId(),
            g.getAssessment().getTitle(),
            g.getAssessment().getCourse().getName(),
            g.getAssessment().getCourse().getCode(),
            g.getCreatedAt().format(fmt),
            finalScore,
            passed ? "Passed" : "Failed",
            g.getAssessment().getType().name()
        );
    })
    .toList();

        return new DashboardStatsDTO(
            totalCourses,
            totalStudents,
            totalAssessments,
            passedRate,
            impact,
            report,
            recent
        );
    }




  @Override
  public List<StudentDTO> getStudents(Long instructorId) {
    var query = em.createQuery(
      """
      SELECT DISTINCT e.student
        FROM CourseInstructor ci
        JOIN ci.course c
        JOIN c.enrollments e
       WHERE ci.instructor.id = :instId
      """,
      Student.class
    ).setParameter("instId", instructorId);

    return query.getResultList()
                .stream()
                .map(studentMapper::toDTO)
                .toList();
  }
@Override
@Transactional(readOnly = true)
public List<AssessmentSummaryDTO> getAssessments(Long instructorId) {
    var ql = """
      SELECT 
        a.id, 
        a.title, 
        a.course.code, 
        a.type.name(), 
        a.dueDate, 
        COUNT(g), 
        AVG(g.finalScore)
      FROM Assessment a
      LEFT JOIN a.submissions g
      WHERE a.instructor.id = :instId
      GROUP BY 
        a.id, 
        a.title, 
        a.course.code, 
        a.type.name(), 
        a.dueDate
    """;

    @SuppressWarnings("unchecked")
    List<Object[]> rows = em.createQuery(ql)
                           .setParameter("instId", instructorId)
                           .getResultList();

    return rows.stream()
      .map(r -> {
          Long    id           = ((Number)    r[0]).longValue();
          String  title        = (String)      r[1];
          String  courseCode   = (String)      r[2];
          String  type         = (String)      r[3];
          // convert whatever timestamp object you get into LocalDateTime:
          LocalDateTime dueDate = r[4] instanceof java.time.LocalDateTime
                                    ? (LocalDateTime) r[4]
                                    : ((java.sql.Timestamp) r[4]).toLocalDateTime();
          long    submissions  = r[5] != null
                                    ? ((Number) r[5]).longValue()
                                    : 0L;
          double  avg          = r[6] != null
                                    ? ((Number) r[6]).doubleValue()
                                    : 0.0;
          return new AssessmentSummaryDTO(
            id, title, courseCode, type, dueDate, submissions, avg
          );
      })
      .toList();
}

    @Override
    public List<DirectoryUserDTO> getUsers(Long instructorId) {
        // (optional) verify instructor exists:
        repo.findById(instructorId)
            .orElseThrow(() -> new ResourceNotFoundException("Instructor " + instructorId + " not found"));

        return userRepo.findAll()
            .stream()
            .map(u -> new DirectoryUserDTO(
                 u.getId(),
                 u.getEmail(),
                 u.getRole().getName()
            ))
            .toList();
    }




  @Override
@Transactional(readOnly = true)
public List<GradebookRowDTO> getGradebook(Long instructorId) {
    String jpql = """
        SELECT new com.example.project.Instructors.GradebookRowDTO(
          g.id,
          g.student.user.firstName,
          g.student.user.lastName,
          g.assessment.course.name,
          g.assessment.title,
          g.finalScore,
          g.assessment.totalScore,
          g.released
        )
        FROM AssessmentGrade g
        WHERE g.assessment.instructor.id = :instId
    """;

    return em.createQuery(jpql, GradebookRowDTO.class)
             .setParameter("instId", instructorId)
             .getResultList();
}



  @Override
public CourseContentDTO saveContent(
    Long instructorId,
    MultipartFile file,
    CourseContentDTO dto
) {
  try {
    var resp = contentService.addCourseContent(dto);
    var created = (CourseContentDTO) resp.getBody();
    fileMetadataService.storeCourseContentFile(created.getId(), file);
    return contentService.one(created.getId()).getContent();
  } catch (IOException e) {
    // wrap or rethrow as an unchecked
    throw new UncheckedIOException(e);
  }
}

}