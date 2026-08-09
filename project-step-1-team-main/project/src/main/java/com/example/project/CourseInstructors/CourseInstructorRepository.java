package com.example.project.CourseInstructors;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.Courses.Course;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

public interface CourseInstructorRepository extends JpaRepository<CourseInstructor, Long> {

     //to Check if an instructor is already assigned to a specific course, semester, and section
boolean existsByInstructorAndCourseAndSection(
    Instructor instructor,
    Course course,
    Section section
);
    
    List<CourseInstructor> findByCourseId(Long courseId);
    List<CourseInstructor> findByInstructorId(Long instructorId);

}
