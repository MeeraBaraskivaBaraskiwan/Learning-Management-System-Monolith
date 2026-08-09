package com.example.project.Enrollments;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.Sections.Section;
import com.example.project.Students.Student;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentAndSection(Student student, Section section);

    Optional<Enrollment> findByStudentAndSection(Student student, Section section);

    List<Enrollment> findAllByStudentId(Long studentId);

    List<Enrollment> findAllBySectionId(Long sectionId);

    List<Enrollment> findAllBySectionCourseId(Long courseId);

     List<Enrollment> findAllByStudent_User_Id(Long userId);
}


