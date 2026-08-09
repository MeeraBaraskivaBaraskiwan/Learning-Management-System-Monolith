package com.example.project.Students;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.project.Users.User;
import java.util.stream.Collectors;
@Component
public class StudentMapper {

    private static final Logger logger = LoggerFactory.getLogger(StudentMapper.class);

       public StudentDTO toDTO(Student student) {
        if (student == null) {
            logger.warn("Attempted to map a null Student entity to DTO");
            return null;
        }

        logger.info("Mapping Student entity with ID: {} to DTO", student.getId());
        StudentDTO dto = new StudentDTO();

        dto.setId(student.getId());
        dto.setStudentId(student.getStudentId());
        dto.setMajor(student.getMajor());

        User user = student.getUser();
        if (user != null) {
            logger.info("Mapping associated User entity with ID: {} to StudentDTO", user.getId());
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
        } else {
            logger.warn("Student entity with ID: {} has no associated User", student.getId());
        }

        // ← NEW: extract enrolled course titles into the DTO
       dto.setCourses(
       student.getEnrollments().stream()
              .map(enrollment -> enrollment.getCourse().getName())   // <-- use getName()
             .collect(Collectors.toList())
   );

        logger.info("Successfully mapped Student entity with ID: {} to DTO", student.getId());
        return dto;
    }


    public Student toEntity(StudentDTO dto, User user) {
        if (dto == null || user == null) {
            logger.warn("Attempted to map a null StudentDTO or User to Student entity");
            return null;
        }

        logger.info("Mapping StudentDTO with studentId: {} and User with ID: {} to Student entity", dto.getStudentId(), user.getId());
       
        Student student = new Student(
            user,
            dto.getStudentId(),
            dto.getMajor()
        );
        logger.info("Successfully mapped StudentDTO with studentId: {} to Student entity", dto.getStudentId());
        return student;
    }
}