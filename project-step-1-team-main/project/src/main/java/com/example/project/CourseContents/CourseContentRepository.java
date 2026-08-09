package com.example.project.CourseContents;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {
    List<CourseContent> findByCourseId(Long courseId);
    List<CourseContent> findByInstructorId(Long instructorId);
}
