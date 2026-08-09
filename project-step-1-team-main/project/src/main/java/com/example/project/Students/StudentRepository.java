package com.example.project.Students;


import java.util.Optional;
import com.example.project.Users.User; // Adjust the package path if necessary

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    
    @EntityGraph(attributePaths = {"user", "user.role"})
    Page<Student> findAll(Pageable pageable);
    
    Optional<Student> findByUser_Id(Long userId); 
    Optional<Student> findByUser(User user);

    @Query("""
    SELECT DISTINCT s
      FROM Student s
      JOIN s.enrollments e
      JOIN e.course c
      JOIN c.courseInstructors ci
     WHERE ci.instructor.id = :instrId
    """)
  List<Student> findEnrolledByInstructorId(@Param("instrId") Long instrId);
}