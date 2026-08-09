package com.example.project.CourseInstructors;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Courses.Course;
import com.example.project.Courses.CourseRepository;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Instructors.Instructor;
import com.example.project.Instructors.InstructorRepository;
import com.example.project.Sections.Section;
import com.example.project.Sections.SectionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CourseInstructorServiceImpl implements CourseInstructorService {
    private static final Logger logger = LoggerFactory.getLogger(CourseInstructorServiceImpl.class);

    private final CourseInstructorRepository courseInstructorRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final CourseInstructorModelAssembler assembler;
    private final CourseInstructorMapper courseInstructorMapper;
private final SectionRepository sectionRepository;

    public CourseInstructorServiceImpl(
            CourseInstructorRepository courseInstructorRepository,
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            CourseInstructorModelAssembler assembler,
            CourseInstructorMapper courseInstructorMapper,SectionRepository sectionRepository) {
        this.courseInstructorRepository = courseInstructorRepository;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.assembler = assembler;
        this.courseInstructorMapper = courseInstructorMapper;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public CollectionModel<EntityModel<CourseInstructorDTO>> all(Pageable pageable) {
        logger.info("Fetching all course instructors with pageable: {}", pageable);
        Page<CourseInstructor> courseInstructorPage = courseInstructorRepository.findAll(pageable);
        List<EntityModel<CourseInstructorDTO>> courseInstructorDTOs = courseInstructorPage.getContent().stream()
                .map(courseInstructorMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(courseInstructorDTOs, 
                linkTo(methodOn(CourseInstructorController.class).all(pageable)).withSelfRel());
    }

    @Override
    public EntityModel<CourseInstructorDTO> one(Long id) {
        logger.info("Fetching course instructor with id: {}", id);
        CourseInstructor courseInstructor = courseInstructorRepository.findById(id)
        .orElseThrow(() -> {
            logger.error("Course Instructor with id {} not found.", id);
            return new ResourceNotFoundException("Course Instructor with ID " + id + " not found.");
        });
        return assembler.toModel(courseInstructorMapper.toDTO(courseInstructor));
    }

   @Override
public ResponseEntity<?> assignInstructorToCourse(CourseInstructorDTO dto) {
    logger.info("Assigning instructor {} to course {} for section {}",
        dto.getInstructorId(), dto.getCourseId(), dto.getSectionId());

    // 1) Lookup instructor
    Instructor instructor = instructorRepository.findById(dto.getInstructorId())
        .orElseThrow(() -> {
            logger.error("Instructor with ID {} not found.", dto.getInstructorId());
            return new ResourceNotFoundException(
                "Instructor with ID " + dto.getInstructorId() + " not found.");
        });

    // 2) Lookup course
    Course course = courseRepository.findById(dto.getCourseId())
        .orElseThrow(() -> {
            logger.error("Course with ID {} not found.", dto.getCourseId());
            return new ResourceNotFoundException(
                "Course with ID " + dto.getCourseId() + " not found.");
        });

    // 3) Lookup section
    Section section = sectionRepository.findById(dto.getSectionId())
        .orElseThrow(() -> {
            logger.error("Section with ID {} not found.", dto.getSectionId());
            return new ResourceNotFoundException(
                "Section with ID " + dto.getSectionId() + " not found.");
        });

    // 4) Prevent duplicates
    if (courseInstructorRepository.existsByInstructorAndCourseAndSection(
            instructor, course, section)) {
        logger.warn("Instructor {} already assigned to Course {} for Section {}",
            dto.getInstructorId(), dto.getCourseId(), dto.getSectionId());
        throw new RuntimeException(
            "Instructor ID " + dto.getInstructorId() +
            " is already assigned to Course ID " + dto.getCourseId() +
            " for Section " + dto.getSectionId());
    }

    // 5) Save the new assignment
    CourseInstructor assignment =
        new CourseInstructor(instructor, course, section);

    CourseInstructor saved = courseInstructorRepository.save(assignment);

    logger.info("Instructor assigned successfully with assignment id: {}", saved.getId());

    // 6) Return 201 Created with the new resource’s URI
    return ResponseEntity
        .created(linkTo(methodOn(CourseInstructorController.class)
            .one(saved.getId()))
        .toUri())
        .body(courseInstructorMapper.toDTO(saved));
}

    
    @Override
    public ResponseEntity<?> removeInstructorFromCourse(Long id) {
        logger.info("Removing course instructor assignment with id: {}", id);
        if (!courseInstructorRepository.existsById(id)) {
            logger.error("Course Instructor with id {} not found.", id);
            throw new ResourceNotFoundException("Course Instructor with ID " + id + " not found.");
        }
        courseInstructorRepository.deleteById(id);
        logger.info("Course instructor assignment with id {} removed successfully.", id);
        return ResponseEntity.ok().build();
    }

    @Override
    public CollectionModel<EntityModel<CourseInstructorDTO>> getInstructorsByCourse(Long courseId) {
        logger.info("Fetching instructors for course id: {}", courseId);
        List<EntityModel<CourseInstructorDTO>> instructors = courseInstructorRepository.findByCourseId(courseId).stream()
                .map(courseInstructorMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(instructors);
    }

    @Override
    public CollectionModel<EntityModel<CourseInstructorDTO>> getCoursesByInstructor(Long instructorId) {
        logger.info("Fetching courses for instructor id: {}", instructorId);
        List<EntityModel<CourseInstructorDTO>> courses = courseInstructorRepository.findByInstructorId(instructorId).stream()
                .map(courseInstructorMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(courses);
    }
}
