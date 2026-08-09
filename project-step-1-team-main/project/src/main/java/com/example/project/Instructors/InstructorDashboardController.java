package com.example.project.Instructors;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
// at the top of InstructorDashboardController.java
import com.example.project.CourseContents.CourseContentDTO;
import com.example.project.CourseContents.CourseContentService;
import com.example.project.Files.FileMetadataService;
import com.example.project.Instructors.DashboardStatsDTO;
import com.example.project.Students.StudentDTO;
import com.example.project.Users.DirectoryUserDTO;
import com.example.project.Users.UserRepository;

import java.io.IOException;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
  import org.springframework.hateoas.EntityModel;
  import java.util.stream.Collectors;
import com.example.project.CourseContents.CourseContentDTO;
import com.example.project.CourseContents.CourseContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/instructors")
@CrossOrigin(origins = "http://localhost:5173")
public class InstructorDashboardController {

  
    private static final Logger logger =
        LoggerFactory.getLogger(InstructorDashboardController.class);

   private final InstructorDashboardService service;
    private final CourseContentService    contentService;
     private final FileMetadataService fileMetadataService;

    // Spring will now inject both beans
     public InstructorDashboardController(
      InstructorDashboardService service,
      CourseContentService       contentService,
      FileMetadataService        fileMetadataService
  ) {
    this.service             = service;
    this.contentService      = contentService;
    this.fileMetadataService = fileMetadataService;
  }

    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    @GetMapping("{id}/dashboard")
    public DashboardStatsDTO dashboard(@PathVariable Long id) {
        return service.getDashboard(id);
    }
  @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
  @GetMapping("/{id}/submissions/recent")
  public List<DashboardStatsDTO.SubmissionRow> recentSubmissions(@PathVariable Long id) {
    // just pull out the “recentSubmissions” slice of your DashboardStatsDTO
    return service.getDashboard(id).recentSubmissions();
  }
 @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
  @GetMapping("/{id}/students")
  public List<StudentDTO> students(@PathVariable Long id) {
    return service.getStudents(id);
  }

  

  @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','STUDENT')")
   @GetMapping("/{id}/assessments")
  public ResponseEntity<?> assessments(@PathVariable Long id) {
    try {
      var list = service.getAssessments(id);
      return ResponseEntity.ok(list);
    } catch (Exception e) {
      // ← this will print your stack-trace to the console:
      logger.error("Error fetching assessments for instructor {}:", id, e);

    
      // send the message back so you can see it in network tab
      return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", e.getClass().getSimpleName(), "message", e.getMessage()));
    }
  }
 @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
  @GetMapping("/{id}/users")
  public List<DirectoryUserDTO> users(@PathVariable Long id) {
    return service.getUsers(id);
  }

@PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
@GetMapping("/{id}/gradebook")
public List<GradebookRowDTO> gradebook(@PathVariable Long id) {
    return service.getGradebook(id);
}

  @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
 @GetMapping("/{id}/content")
  public List<CourseContentDTO> getContent(@PathVariable Long id) {
    // delegate to your CourseContentService, unwrap the EntityModel and return raw DTOs
    return contentService
      .getContentByInstructor(id)
      .getContent()
      .stream()
      .map(EntityModel::getContent)
      .collect(Collectors.toList());
  }



    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
  @PostMapping("/{id}/content")
  public ResponseEntity<CourseContentDTO> uploadContent(
      @PathVariable Long id,
      @RequestParam Long courseId,
      @RequestParam Long sectionId,
      @RequestPart("file") MultipartFile file
  ) throws IOException {
    // build a minimal DTO carrying the FK relationships + title
    var dto = new CourseContentDTO();
    dto.setInstructorId(id);
    dto.setCourseId(courseId);
    dto.setSectionId(sectionId);
    dto.setTitle(file.getOriginalFilename());

    // delegate to service
    CourseContentDTO result = service.saveContent(id, file, dto);
    return ResponseEntity.ok(result);
  }






}