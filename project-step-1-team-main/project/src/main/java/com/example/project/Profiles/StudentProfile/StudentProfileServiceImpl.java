package com.example.project.Profiles.StudentProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Profiles.InstructorProfile.InstructorProfile;
import com.example.project.Profiles.InstructorProfile.InstructorProfileRepository;
import com.example.project.Profiles.Profile.Profile;
import com.example.project.Profiles.Profile.ProfileRepository;
import com.example.project.Students.Student;
import com.example.project.Students.StudentRepository;

@Service
public class StudentProfileServiceImpl implements StudentProfileService {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileServiceImpl.class);

    private final StudentProfileRepository studentProfileRepository;
    private final ProfileRepository profileRepository;
    private final StudentProfileMapper studentProfileMapper;
    private final StudentProfileAssembler studentProfileAssembler;
    private final PagedResourcesAssembler<StudentProfileDTO> pagedAssembler;
    private final InstructorProfileRepository instructorProfileRepository;
    private final StudentRepository studentRepository;

    public StudentProfileServiceImpl(StudentProfileRepository studentProfileRepository,
                                     ProfileRepository profileRepository,
                                     StudentProfileMapper studentProfileMapper,
                                     StudentProfileAssembler studentProfileAssembler,
                                     PagedResourcesAssembler<StudentProfileDTO> pagedAssembler,
                                     InstructorProfileRepository instructorProfileRepository,
                                     StudentRepository studentRepository) {
        this.studentProfileRepository = studentProfileRepository;
        this.profileRepository = profileRepository;
        this.studentProfileMapper = studentProfileMapper;
        this.studentProfileAssembler = studentProfileAssembler;
        this.pagedAssembler = pagedAssembler;
        this.instructorProfileRepository = instructorProfileRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ResponseEntity<EntityModel<StudentProfileDTO>> createStudentProfile(StudentProfileDTO dto) {
        logger.info("Creating a new student profile for studentId: {}", dto.getStudentId());

        if (studentProfileRepository.findByStudentId(dto.getStudentId()).isPresent()) {
            logger.warn("Student profile already exists for studentId: {}", dto.getStudentId());
            throw new AlreadyExistsException("Student profile already exists for student id " + dto.getStudentId());
        }

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> {
                    logger.error("Profile with ID: {} not found", dto.getProfileId());
                    return new ResourceNotFoundException("Profile with ID " + dto.getProfileId() + " not found");
                });

        InstructorProfile advisor = instructorProfileRepository.findById(dto.getAdvisorId())
                .orElseThrow(() -> {
                    logger.error("Advisor (InstructorProfile) with ID: {} not found", dto.getAdvisorId());
                    return new ResourceNotFoundException("Advisor (InstructorProfile) with ID " + dto.getAdvisorId() + " not found");
                });

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> {
                    logger.error("Student with ID: {} not found", dto.getStudentId());
                    return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
                });

        StudentProfile entity = studentProfileMapper.toEntity(dto, profile, advisor, student);
        StudentProfile saved = studentProfileRepository.save(entity);
        logger.info("Successfully created student profile for studentId: {}", dto.getStudentId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentProfileAssembler.toModel(studentProfileMapper.toDTO(saved)));
    }

    @Override
    public EntityModel<StudentProfileDTO> getStudentProfileById(Long id) {
        logger.info("Fetching student profile with ID: {}", id);
        StudentProfile entity = studentProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("StudentProfile with ID: {} not found", id);
                    return new ResourceNotFoundException("StudentProfile with ID " + id + " not found");
                });
        logger.info("Successfully fetched student profile with ID: {}", id);
        return studentProfileAssembler.toModel(studentProfileMapper.toDTO(entity));
    }

    @Override
    public EntityModel<StudentProfileDTO> getStudentProfileByStudentId(Long studentId) {
        logger.info("Fetching student profile for studentId: {}", studentId);
        StudentProfile entity = studentProfileRepository.findByStudentId(studentId)
                .orElseThrow(() -> {
                    logger.error("StudentProfile not found for studentId: {}", studentId);
                    return new ResourceNotFoundException("StudentProfile not found for studentId " + studentId);
                });
        logger.info("Successfully fetched student profile for studentId: {}", studentId);
        return studentProfileAssembler.toModel(studentProfileMapper.toDTO(entity));
    }

    @Override
    public PagedModel<EntityModel<StudentProfileDTO>> getAllStudentProfiles(Pageable pageable) {
        logger.info("Fetching all student profiles with pageable: {}", pageable);
        PagedModel<EntityModel<StudentProfileDTO>> profiles = pagedAssembler.toModel(
                studentProfileRepository.findAll(pageable).map(studentProfileMapper::toDTO),
                studentProfileAssembler);
        logger.info("Successfully fetched all student profiles");
        return profiles;
    }

    @Override
    public ResponseEntity<EntityModel<StudentProfileDTO>> updateStudentProfile(Long id, StudentProfileDTO dto) {
        logger.info("Updating student profile with ID: {}", id);

        StudentProfile entity = studentProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("StudentProfile with ID: {} not found", id);
                    return new ResourceNotFoundException("StudentProfile with ID " + id + " not found");
                });

        Profile profile = profileRepository.findById(dto.getProfileId())
                .orElseThrow(() -> {
                    logger.error("Profile with ID: {} not found", dto.getProfileId());
                    return new ResourceNotFoundException("Profile with ID " + dto.getProfileId() + " not found");
                });

        InstructorProfile advisor = instructorProfileRepository.findById(dto.getAdvisorId())
                .orElseThrow(() -> {
                    logger.error("Advisor (InstructorProfile) with ID: {} not found", dto.getAdvisorId());
                    return new ResourceNotFoundException("Advisor (InstructorProfile) with ID " + dto.getAdvisorId() + " not found");
                });

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> {
                    logger.error("Student with ID: {} not found", dto.getStudentId());
                    return new ResourceNotFoundException("Student with ID " + dto.getStudentId() + " not found");
                });

        studentProfileMapper.updateEntity(entity, dto, profile, advisor, student);
        StudentProfile updated = studentProfileRepository.save(entity);
        logger.info("Successfully updated student profile with ID: {}", id);
        return ResponseEntity.ok(studentProfileAssembler.toModel(studentProfileMapper.toDTO(updated)));
    }

    @Override
    public ResponseEntity<?> deleteStudentProfile(Long id) {
        logger.info("Deleting student profile with ID: {}", id);
        StudentProfile entity = studentProfileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("StudentProfile with ID: {} not found", id);
                    return new ResourceNotFoundException("StudentProfile with ID " + id + " not found");
                });
        studentProfileRepository.delete(entity);
        logger.info("Successfully deleted student profile with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}