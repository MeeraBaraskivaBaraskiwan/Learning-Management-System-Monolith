package com.example.project.CourseContents;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Sinks;

import com.example.project.Courses.Course;
import com.example.project.Courses.CourseRepository;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Instructors.Instructor;
import com.example.project.Instructors.InstructorRepository;
import com.example.project.Sections.Section;
import com.example.project.Sections.SectionRepository;

@Service
public class CourseContentServiceImpl implements CourseContentService {
    private static final Logger logger = LoggerFactory.getLogger(CourseContentServiceImpl.class);

    private final CourseContentRepository courseContentRepository;
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final CourseContentModelAssembler assembler;
    private final CourseContentMapper courseContentMapper;
    private final Sinks.Many<CourseContentDTO> courseContentSink;


    public CourseContentServiceImpl(
            CourseContentRepository courseContentRepository,
            InstructorRepository instructorRepository,
            CourseRepository courseRepository,
            SectionRepository sectionRepository,
            CourseContentModelAssembler assembler,
            CourseContentMapper courseContentMapper,
             Sinks.Many<CourseContentDTO> courseContentSink) {
        this.courseContentRepository = courseContentRepository;
        this.instructorRepository = instructorRepository;
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.assembler = assembler;
        this.courseContentMapper = courseContentMapper;
         this.courseContentSink       = courseContentSink;
    }

    @Override
    public CollectionModel<EntityModel<CourseContentDTO>> all(Pageable pageable) {
        logger.info("Fetching all course contents with pageable: {}", pageable);

        Page<CourseContent> contents = courseContentRepository.findAll(pageable);
        List<EntityModel<CourseContentDTO>> contentDTOs = contents.getContent().stream()
                .map(courseContentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(contentDTOs, linkTo(methodOn(CourseContentController.class).all(pageable)).withSelfRel());
    }

    @Override
    public EntityModel<CourseContentDTO> one(Long id) {
        logger.info("Fetching course content with id: {}", id);
        CourseContent content = courseContentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Course content with ID {} not found.", id);
                    return new ResourceNotFoundException("Course content with ID " + id + " not found.");
                });
        return assembler.toModel(courseContentMapper.toDTO(content));
    }

    @Override
    public ResponseEntity<?> addCourseContent(CourseContentDTO dto) {
        logger.info("Adding course content for course id: {} by instructor id: {}", dto.getCourseId(), dto.getInstructorId());
        if (dto.getInstructorId() == null) {
            logger.error("Instructor ID is null.");
            throw new IllegalArgumentException("Instructor ID cannot be null.");
        }
        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> {
                    logger.error("Instructor with ID {} not found.", dto.getInstructorId());
                    return new ResourceNotFoundException("Instructor with ID " + dto.getInstructorId() + " not found.");
                });

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> {
                    logger.error("Course with ID {} not found.", dto.getCourseId());
                    return new ResourceNotFoundException("Course with ID " + dto.getCourseId() + " not found.");
                });

        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found: " + dto.getSectionId()));

        CourseContent content = new CourseContent(course, instructor, dto.getTitle(), section);
        courseContentRepository.save(content);
        logger.info("Course content added successfully with id: {}", content.getId());
        CourseContentDTO pushed = courseContentMapper.toDTO(content);
        courseContentSink.tryEmitNext(pushed);
        return ResponseEntity.ok(courseContentMapper.toDTO(content));
    }

    @Override
    public ResponseEntity<?> deleteCourseContent(Long id) {
        logger.info("Deleting course content with id: {}", id);
        if (!courseContentRepository.existsById(id)) {
            logger.error("Course content with ID {} not found.", id);
            throw new ResourceNotFoundException("Course content with ID " + id + " not found.");
        }
        courseContentRepository.deleteById(id);
        logger.info("Course content with id {} deleted successfully.", id);
        return ResponseEntity.ok().build();
    }

    @Override
    public void deleteByInstructorAndContentId(Long instructorId, Long contentId) {
        CourseContent cc = courseContentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found: " + contentId));

        if (!cc.getInstructor().getId().equals(instructorId)) {
            throw new AccessDeniedException("Not your content");
        }
        courseContentRepository.delete(cc);
    }

    @Override
    public CollectionModel<EntityModel<CourseContentDTO>> getContentByCourse(Long courseId) {
        logger.info("Fetching course contents for course id: {}", courseId);
        List<EntityModel<CourseContentDTO>> contentDTOs = courseContentRepository.findByCourseId(courseId).stream()
                .map(courseContentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(contentDTOs);
    }

    @Override
    public CollectionModel<EntityModel<CourseContentDTO>> getContentByInstructor(Long instructorId) {
        logger.info("Fetching course contents for instructor id: {}", instructorId);
        List<EntityModel<CourseContentDTO>> contentDTOs = courseContentRepository.findByInstructorId(instructorId).stream()
                .map(courseContentMapper::toDTO)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(contentDTOs);
    }

    


    @Override
    public List<CourseContentDTO> getPlainContentByCourse(Long courseId) {
        return courseContentRepository.findByCourseId(courseId)
            .stream()
            .map(courseContentMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public CourseContentDTO createContentEntry(CourseContentDTO dto) {
        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        CourseContent content = new CourseContent(course, instructor, dto.getTitle(), section);
        content = courseContentRepository.save(content);
        return courseContentMapper.toDTO(content);
    }

    @Override
    public CourseContentDTO editContentEntry(Long contentId, CourseContentDTO dto) {
        CourseContent content = courseContentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
        if (dto.getTitle() != null) content.setTitle(dto.getTitle());
        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
            content.setSection(section);
        }
        courseContentRepository.save(content);
        return courseContentMapper.toDTO(content);
    }

    @Override
    public void deleteContentEntry(Long contentId) {
        CourseContent content = courseContentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found"));
        // Optionally: delete associated files physically if needed
        courseContentRepository.delete(content);
    }}