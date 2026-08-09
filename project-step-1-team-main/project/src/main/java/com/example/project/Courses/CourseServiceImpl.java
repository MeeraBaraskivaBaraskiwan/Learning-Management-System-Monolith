package com.example.project.Courses;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.ResourceNotFoundException;

@Service

    public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final CourseModelAssembler assembler;
    private final CourseMapper courseMapper;
    
    public CourseServiceImpl(CourseRepository courseRepository, CourseModelAssembler assembler, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.assembler = assembler;
        this.courseMapper = courseMapper;
    }
    
    @Override
    public CollectionModel<EntityModel<CourseDTO>> all(Pageable pageable) {
        logger.info("Fetching all courses with pageable: {}", pageable);
        Page<Course> coursesPage = courseRepository.findAll(pageable);
        List<EntityModel<CourseDTO>> courseDTOs = coursesPage.getContent().stream()
                .map(courseMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(courseDTOs, linkTo(methodOn(CourseController.class).all(pageable)).withSelfRel());
    }

    @Override
    public ResponseEntity<?> newCourse(CourseDTO newCourseDTO) {
        logger.info("Attempting to create course with code: {}", newCourseDTO.getCode());

    // First Check if a course with the same code exists then continue creating a new course
    if (courseRepository.findByCode(newCourseDTO.getCode()).isPresent()) {
        logger.warn("Course with code {} already exists.", newCourseDTO.getCode());
        return ResponseEntity.badRequest().body("Course with code " + newCourseDTO.getCode() + " already exists.");
    }
    Course course = courseMapper.toEntity(newCourseDTO);
    courseRepository.save(course);
    logger.info("Course created successfully with id: {}", course.getId());
    return ResponseEntity.ok(courseMapper.toDTO(course));
    }


    @Override
    public EntityModel<CourseDTO> one(Long id) {
        logger.info("Fetching course with id: {}", id);

        Course course = courseRepository.findById(id).orElseThrow(() -> {
            logger.error("Course with id {} not found.", id);
            return new ResourceNotFoundException("Course with ID " + id + " not found.");
        });
        return assembler.toModel(courseMapper.toDTO(course));
    }

    @Override
    public ResponseEntity<?> updateCourse(CourseDTO updatedCourseDTO, Long id) {
        logger.info("Updating course with id: {}", id);

        Course course = courseRepository.findById(id).orElseThrow(() -> {
            logger.error("Course with id {} not found for update.", id);
            return new ResourceNotFoundException("Course with ID " + id + " not found.");
        });        course.setCode(updatedCourseDTO.getCode());
        course.setName(updatedCourseDTO.getName());
        course.setDescription(updatedCourseDTO.getDescription());
        course.setCredits(updatedCourseDTO.getCredits());
        courseRepository.save(course);
        logger.info("Course with id {} updated successfully.", id);
        return ResponseEntity.ok(courseMapper.toDTO(course));
    }

    @Override
    public ResponseEntity<?> deleteCourse(Long id) {
        logger.info("Deleting course with id: {}", id);
        if (!courseRepository.existsById(id)) {
            logger.warn("Course with id {} does not exist.", id);
            return ResponseEntity.badRequest().body("Course with ID " + id + " does not exist.");
        }
        courseRepository.deleteById(id);
        logger.info("Course with id {} deleted successfully.", id);
        return ResponseEntity.ok().build();
    }
}

