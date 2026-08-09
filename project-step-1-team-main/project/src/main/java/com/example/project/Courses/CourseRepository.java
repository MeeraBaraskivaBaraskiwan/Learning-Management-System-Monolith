package com.example.project.Courses;

import java.util.Optional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
    Page<Course> findAll(Pageable pageable); 

     @Query("""
      SELECT COUNT(ci)
        FROM CourseInstructor ci
       WHERE ci.instructor.id = :instructorId
      """)
    long countByInstructorId(@Param("instructorId") Long instructorId);

    /**
     * Fetch the actual Course entities taught by that instructor
     * by selecting ci.course from the join‐table.
     */
    @Query("""
      SELECT ci.course
        FROM CourseInstructor ci
       WHERE ci.instructor.id = :instructorId
      """)
    List<Course> findByInstructorId(@Param("instructorId") Long instructorId);

}