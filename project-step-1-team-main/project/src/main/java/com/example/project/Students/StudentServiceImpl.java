package com.example.project.Students;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.User;

import com.example.project.Users.UserRepository;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class StudentServiceImpl implements StudentService {


    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentModelAssembler assembler;
    private final UserRepository userRepository;

  

    public StudentServiceImpl(StudentModelAssembler assembler, StudentMapper studentMapper, StudentRepository studentRepository, UserRepository userRepository) {
        this.assembler = assembler;
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }


    @Override
    public CollectionModel<EntityModel<StudentDTO>> all(Pageable pageable) {
        logger.info("Fetching all students with pageable: {}", pageable);
       
        Page<Student> studentsPage = studentRepository.findAll(pageable);
        List<EntityModel<StudentDTO>> studentDTOs = studentsPage.getContent().stream()
                .map(studentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

                logger.info("Successfully fetched all students");
       
        return CollectionModel.of(studentDTOs, linkTo(methodOn(StudentController.class).all(pageable)).withSelfRel());
    }

       @Override
    public ResponseEntity<?> newStudent(StudentDTO newStudentDTO) {
      
        logger.info("Creating a new student with studentId: {}", newStudentDTO.getStudentId());
       
        if (studentRepository.findByStudentId(newStudentDTO.getStudentId()).isPresent()) {
            logger.warn("A student with studentId: {} already exists", newStudentDTO.getStudentId());
           
            throw new AlreadyExistsException(
                "A student with ID " + newStudentDTO.getStudentId() + " already exists"
            );
        }


        User user = userRepository.findById(newStudentDTO.getUserId())
        .orElseThrow(() -> {
            logger.error("User with ID: {} not found", newStudentDTO.getUserId());
            return new ResourceNotFoundException(
                "User with ID " + newStudentDTO.getUserId() + " not found"
            );
        });


        logger.info("Updating user details for userId: {}", user.getId());
      
            user.setEmail(newStudentDTO.getEmail());
            user.setFirstName(newStudentDTO.getFirstName());
            user.setLastName(newStudentDTO.getLastName());
            userRepository.save(user);
        
            Student student = studentMapper.toEntity(newStudentDTO, user);
            studentRepository.save(student);
        
            logger.info("Successfully created a new student with studentId: {}", newStudentDTO.getStudentId());
       
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(assembler.toModel(studentMapper.toDTO(student)));
        }

        
    @Override
    public EntityModel<StudentDTO> one(Long id) {
        logger.info("Fetching student with ID: {}", id);
        
        Student student = studentRepository.findById(id).orElseThrow(() -> {
            logger.error("Student with ID: {} not found", id);
            return new ResourceNotFoundException("Student with ID " + id + " not found");
        });
        logger.info("Successfully fetched student with ID: {}", id);
        return assembler.toModel(studentMapper.toDTO(student));
    }

    @Override
    public ResponseEntity<?> updateStudent(StudentDTO updatedStudentDTO, Long id) {
        logger.info("Updating student with ID: {}", id);
        
        return studentRepository.findById(id)
            .map(student -> {
               
                logger.info("Found student with ID: {}, updating details", id);
                
                student.setStudentId(updatedStudentDTO.getStudentId());
                student.setMajor(updatedStudentDTO.getMajor());

                User user = userRepository.findById(updatedStudentDTO.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID: {} not found", updatedStudentDTO.getUserId());
                    return new ResourceNotFoundException(
                        "User with ID " + updatedStudentDTO.getUserId() + " not found"
                    );
                });

                logger.info("Updating user details for userId: {}", user.getId());
               
                    user.setEmail(updatedStudentDTO.getEmail());
                    user.setFirstName(updatedStudentDTO.getFirstName());
                    user.setLastName(updatedStudentDTO.getLastName());
                    userRepository.save(user);

                    student.setUser(user);
                    studentRepository.save(student);
                    logger.info("Successfully updated student with ID: {}", id);
                
                return ResponseEntity.ok(studentMapper.toDTO(student));
            })
            .orElseThrow(() -> {
                logger.error("Student with ID: {} not found", id);
                return new ResourceNotFoundException("Student with ID " + id + " not found");
            });
    }

    @Override
    public ResponseEntity<?> deleteStudent(Long id) {
        logger.info("Deleting student with ID: {}", id);
        
        if (!studentRepository.existsById(id)) {
            logger.error("Student with ID: {} not found", id);
            throw new ResourceNotFoundException("Student with ID " + id + " not found");
        }
        studentRepository.deleteById(id);
        logger.info("Successfully deleted student with ID: {}", id);
        
        return ResponseEntity.ok().build();
    }

    @Override
    public EntityModel<StudentDTO> findByStudentId(String studentId) {
        logger.info("Fetching student with studentId: {}", studentId);
        Student student = studentRepository.findByStudentId(studentId)
            .orElseThrow(() -> {
                logger.error("Student with studentId: {} not found", studentId);
                return new ResourceNotFoundException(
                    "Student with studentId " + studentId + " not found"
                );
            });
        logger.info("Successfully fetched student with studentId: {}", studentId);
        return assembler.toModel(studentMapper.toDTO(student));
    }

    @Override
public EntityModel<StudentDTO> findByUserId(Long userId) {
    logger.info("Looking up student for userId={}", userId);
    Student student = studentRepository
        .findByUser_Id(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Not a student"));
    return assembler.toModel(studentMapper.toDTO(student));
}

  @Override
  public List<StudentDTO> simpleAll() {
    return studentRepository.findAll().stream()
      .map(studentMapper::toDTO)
      .collect(Collectors.toList());
  }


   @Override
  public List<StudentDTO> simpleEnrolledByInstructor(Long instrId) {
    return studentRepository.findEnrolledByInstructorId(instrId)
               .stream()
               .map(studentMapper::toDTO)
               .collect(Collectors.toList());
  }
}
