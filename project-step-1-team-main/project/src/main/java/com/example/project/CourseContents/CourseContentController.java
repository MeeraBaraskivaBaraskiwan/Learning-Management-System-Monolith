package com.example.project.CourseContents;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;

import com.example.project.Files.FileMetadataDTO;
import com.example.project.Files.FileMetadataService;


import java.util.List;

@Tag(name = "Course Content Management", description = "Operations related to course content management")
@RestController
@RequestMapping("/course-contents")
public class CourseContentController {
    private static final Logger logger = LoggerFactory.getLogger(CourseContentController.class);

    private final CourseContentService courseContentService;
    private final FileMetadataService fileService;


     private final Sinks.Many<CourseContentDTO> courseContentSink;
    public CourseContentController(CourseContentService courseContentService,Sinks.Many<CourseContentDTO> courseContentSink, FileMetadataService fileService) {
        this.courseContentService = courseContentService;
        this.fileService = fileService;
        this.courseContentSink  = courseContentSink;

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve all course contents", description = "Returns a paginated list of all course contents.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course contents")
    })
    @GetMapping
    public CollectionModel<EntityModel<CourseContentDTO>> all(Pageable pageable) {
        logger.info("Fetching all course contents with pageable: {}", pageable);
        return courseContentService.all(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Retrieve a course content by ID", description = "Fetches course content details using the content ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course content"),
        @ApiResponse(responseCode = "404", description = "Course content not found")
    })
    @GetMapping("/{id}")
    public EntityModel<CourseContentDTO> one(@PathVariable Long id) {
        logger.info("Fetching course content with id: {}", id);
        return courseContentService.one(id);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Add course content", description = "Adds new course content.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course content added successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input")
    })
    @PostMapping
    public ResponseEntity<?> addCourseContent(@Valid @RequestBody CourseContentDTO dto) {
        logger.info("Adding new course content: {}", dto);
        return courseContentService.addCourseContent(dto);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    @Operation(summary = "Delete course content", description = "Deletes course content by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course content deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Course content not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourseContent(@PathVariable Long id) {
        logger.info("Deleting course content with id: {}", id);
        return courseContentService.deleteCourseContent(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get course contents by course", description = "Retrieves a list of course contents for a specific course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course contents")
    })
    @GetMapping("/course/{courseId}")
    public CollectionModel<EntityModel<CourseContentDTO>> getContentByCourse(@PathVariable Long courseId) {
        logger.info("Fetching course contents for course id: {}", courseId);
        return courseContentService.getContentByCourse(courseId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @DeleteMapping("/instructors/{instructorId}/content/{contentId}")
    public ResponseEntity<Void> deleteByInstructor(
        @PathVariable Long instructorId,
        @PathVariable Long contentId
    ) {
        logger.info("Deleting content with id: {} for instructor id: {}", contentId, instructorId);
        courseContentService.deleteByInstructorAndContentId(instructorId, contentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    @Operation(summary = "Get course contents by instructor", description = "Retrieves a list of course contents by a specific instructor.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved course contents")
    })
    @GetMapping("/instructor/{instructorId}")
    public CollectionModel<EntityModel<CourseContentDTO>> getContentByInstructor(@PathVariable Long instructorId) {
        logger.info("Fetching course contents for instructor id: {}", instructorId);
        return courseContentService.getContentByInstructor(instructorId);
    }



    // ─── SSE endpoint for live course-content pushes ────────────────
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/stream/course/{courseId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CourseContentDTO> streamCourseContent(@PathVariable Long courseId) {
      return courseContentSink.asFlux()
                              .filter(dto -> dto.getCourseId().equals(courseId));

    }

    // Get all contents for a course, plain JSON (NO HATEOAS)
    @GetMapping("/plain/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    public List<CourseContentDTO> getPlainByCourse(@PathVariable Long courseId) {
        logger.info("Fetching plain course contents for course id: {}", courseId);
        return courseContentService.getPlainContentByCourse(courseId);
    }

    // Create content entry
    @PostMapping("/plain")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseContentDTO> createPlain(@RequestBody @Valid CourseContentDTO dto) {
        logger.info("Creating new course content: {}", dto);
        return ResponseEntity.ok(courseContentService.createContentEntry(dto));
    }

    // Edit content entry (title, section, etc)
    @PutMapping("/plain/{contentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<CourseContentDTO> editPlain(
            @PathVariable Long contentId,
            @RequestBody @Valid CourseContentDTO dto
    ) {
        logger.info("Editing course content with id: {}", contentId);
        return ResponseEntity.ok(courseContentService.editContentEntry(contentId, dto));
    }

    // Delete content entry (and all its files)
    @DeleteMapping("/plain/{contentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Void> deletePlain(@PathVariable Long contentId) {
        logger.info("Deleting course content with id: {}", contentId);
        courseContentService.deleteContentEntry(contentId);
        return ResponseEntity.noContent().build();
    }

    // Upload file for a content entry (returns file metadata)
    @PostMapping("/{contentId}/upload")
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    public ResponseEntity<FileMetadataDTO> uploadFile(
            @PathVariable Long contentId,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        logger.info("Uploading file for course content id: {}", contentId);
        FileMetadataDTO fileDto = fileService.storeCourseContentFile(contentId, file).getContent();
        return ResponseEntity.ok(fileDto);

    }
}
