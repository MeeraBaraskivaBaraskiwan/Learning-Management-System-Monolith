package com.example.project.CourseInstructors;

import org.springframework.stereotype.Component;

import com.example.project.Courses.Course;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

@Component
public class CourseInstructorMapper {
  public CourseInstructorDTO toDTO(CourseInstructor e) {
    var dto = new CourseInstructorDTO();
    dto.setId(e.getId());
    dto.setInstructorId(e.getInstructor().getId());
    dto.setCourseId(e.getCourse().getId());
    dto.setSectionId(e.getSection().getId());
    return dto;
  }

  public CourseInstructor toEntity(CourseInstructorDTO dto,
                                   Instructor ins,
                                   Course course,
                                   Section section) {
    return new CourseInstructor(ins, course, section);
  }
}

