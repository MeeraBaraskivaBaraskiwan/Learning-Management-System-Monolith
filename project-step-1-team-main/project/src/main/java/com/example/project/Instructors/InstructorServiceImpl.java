package com.example.project.Instructors;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InstructorServiceImpl implements InstructorService {

    private static final Logger logger = LoggerFactory.getLogger(InstructorServiceImpl.class);
    
    private InstructorRepository instructorRepository;
    private InstructorModelAssembler assembler;
    private InstructorMapper instructorMapper;
    private UserRepository userRepository;

    public InstructorServiceImpl(InstructorRepository instructorRepository,
                                 InstructorModelAssembler assembler,
                                 InstructorMapper instructorMapper,
                                 UserRepository userRepository) {
        this.instructorRepository = instructorRepository;
        this.assembler = assembler;
        this.instructorMapper = instructorMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CollectionModel<EntityModel<InstructorDTO>> all(Pageable pageable) {
        logger.info("Fetching all instructors with pagination: {}", pageable);
        Page<Instructor> instructorsPage = instructorRepository.findAll(pageable);
        List<EntityModel<InstructorDTO>> instructorDTOs = instructorsPage.getContent().stream()
                .map(instructorMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(instructorDTOs, linkTo(methodOn(InstructorController.class).all(pageable)).withSelfRel());
    }


  @Override
public ResponseEntity<?> newInstructor(InstructorDTO newInstructorDTO) {
    logger.info("Creating a new instructor with ID: {}", newInstructorDTO.getInstructorId());
    if (instructorRepository.findByInstructorId(newInstructorDTO.getInstructorId()).isPresent()) {
        throw new AlreadyExistsException(
            "An instructor with ID " + newInstructorDTO.getInstructorId() + " already exists."
        );
    }

    User user = userRepository.findById(newInstructorDTO.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User with ID " + newInstructorDTO.getUserId() + " not found."));

    user.setEmail(newInstructorDTO.getEmail());
    user.setFirstName(newInstructorDTO.getFirstName());
    user.setLastName(newInstructorDTO.getLastName());
    userRepository.save(user);

    Instructor instructor = instructorMapper.toEntity(newInstructorDTO, user);
    instructorRepository.save(instructor);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(assembler.toModel(instructorMapper.toDTO(instructor)));
}

    @Override
    public EntityModel<InstructorDTO> one(Long id) {
        logger.info("Fetching instructor with ID: {}", id);
        Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor with ID " + id + " not found."));
        return assembler.toModel(instructorMapper.toDTO(instructor));
    }

    @Override
    public ResponseEntity<?> updateInstructor(InstructorDTO updatedInstructorDTO, Long id) {
        logger.info("Updating instructor with ID: {}", id);
        Instructor instructor = instructorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Instructor with ID " + id + " not found."));
        User user = findUserById(updatedInstructorDTO.getUserId());
        instructor.setUser(user);
        instructor.setInstructorId(updatedInstructorDTO.getInstructorId()); 
        instructor.setFaculty(updatedInstructorDTO.getFaculty());        
        instructor.setDepartment(updatedInstructorDTO.getDepartment());   

        instructorRepository.save(instructor);
        return ResponseEntity.ok(instructorMapper.toDTO(instructor));
    }

    @Override
    public ResponseEntity<?> deleteInstructor(Long id) {
        logger.info("Deleting instructor with ID: {}", id);
        if (!instructorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Instructor with ID " + id + " not found.");
        }
        instructorRepository.deleteById(id);
        return ResponseEntity.noContent().build(); 
    }

    public EntityModel<InstructorDTO> findByInstructorId(String instructorId) {
        logger.info("Fetching instructor with instructor ID: {}", instructorId);
        Instructor instructor = instructorRepository.findByInstructorId(instructorId)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with ID: " + instructorId));
        return assembler.toModel(instructorMapper.toDTO(instructor));
    }
    
    private User findUserById(Long userId) {
        logger.debug("Looking for user with ID: {}", userId);
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found."));
    }


    @Override
    public EntityModel<InstructorDTO> findByUserId(Long userId) {
        Instructor inst = instructorRepository
            .findByUser_Id(userId)
            .orElseThrow(() -> 
                new ResourceNotFoundException("No instructor found for user ID " + userId)
            );
        return assembler.toModel(instructorMapper.toDTO(inst));
    }

}

