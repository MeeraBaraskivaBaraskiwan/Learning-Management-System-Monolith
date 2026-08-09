package com.example.project.Students;


import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import java.util.List;
public interface StudentService {
    CollectionModel<EntityModel<StudentDTO>> all(Pageable pageable);

    ResponseEntity<?> newStudent(StudentDTO newStudent);

    EntityModel<StudentDTO> one(Long id);

    ResponseEntity<?> updateStudent(StudentDTO newStudent, Long id);

    ResponseEntity<?> deleteStudent(Long id);

    EntityModel<StudentDTO> findByStudentId(String studentId);

    EntityModel<StudentDTO> findByUserId(Long userId);

     List<StudentDTO> simpleAll();

     List<StudentDTO> simpleEnrolledByInstructor(Long instructorId);
 }