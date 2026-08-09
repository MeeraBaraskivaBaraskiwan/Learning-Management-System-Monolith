package com.example.project.Instructors;

import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.project.Courses.CourseDTO;
import com.example.project.Courses.CourseMapper;
import com.example.project.Courses.CourseRepository;

@RestController
@RequestMapping("/instructors")
@CrossOrigin(origins = "http://localhost:5173")   // ← allow your Vite front-end
public class InstructorCourseController {

  private final CourseRepository courseRepo;
  private final CourseMapper     mapper;

  public InstructorCourseController(CourseRepository courseRepo,
                                    CourseMapper mapper) {
    this.courseRepo = courseRepo;
    this.mapper     = mapper;
  }

  @GetMapping("/{instrId}/courses")
  @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
  public List<CourseDTO> getCoursesByInstructor(@PathVariable Long instrId) {
    return courseRepo
      .findByInstructorId(instrId)          // uses your JPA @Query
      .stream()
      .map(mapper::toDTO)                    // Course → CourseDTO
      .collect(Collectors.toList());
  }
}