package com.example.project.Assessments.Assessments_Assessment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;

@Tag(name = "Assessments", description = "Endpoints for managing assessments")
@RestController
@RequestMapping("/assessments")
public class AssessmentController {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentController.class); // ✅ LOGGER DECLARATION

    private final AssessmentService assessmentService;
    private final Sinks.Many<AssessmentDTO> assessmentSink;

    public AssessmentController(AssessmentService assessmentService,Sinks.Many<AssessmentDTO> assessmentSink) {
        this.assessmentService = assessmentService;
        this.assessmentSink    = assessmentSink;
    }

    
    @Operation(summary = "Get all assessments", description = "Fetches a paginated list of all assessments.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved assessments")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<AssessmentDTO>> getAllAssessments(
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
     logger.info("Fetching all assessments with pagination: {}", pageable);
        return assessmentService.getAllAssessments(pageable);
    }
   

    @Operation(summary = "Get assessment by ID", description = "Fetches a specific assessment by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assessment found"),
        @ApiResponse(responseCode = "404", description = "Assessment not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/{id}")
    public EntityModel<AssessmentDTO> getAssessmentById(@PathVariable Long id) {
        logger.info("Fetching assessment by ID: {}", id); 
        return assessmentService.getAssessmentById(id);
    }

    

    @Operation(summary = "Get assessments by course code", description = "Fetches all assessments for a specific course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assessments found"),
        @ApiResponse(responseCode = "404", description = "No assessments found for the course")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','STUDENT')")
    @GetMapping("/course/{courseCode}")
    public PagedModel<EntityModel<AssessmentDTO>> getAssessmentsByCourseCode(
            @PathVariable String courseCode,
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
        logger.info("Fetching assessments by course code: {}", courseCode);
        return assessmentService.getAssessmentsByCourseCode(courseCode, pageable);
    }



    @Operation(summary = "Create a new assessment", description = "Adds a new assessment to the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Assessment created"),
        @ApiResponse(responseCode = "404", description = "Course or Instructor not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createAssessment(@Valid @RequestBody AssessmentDTO dto) {
        logger.info("Creating new assessment for course {} by instructor {}", dto.getCourseCode(), dto.getInstructorId()); 
        return assessmentService.createAssessment(dto);
    }
    

    @Operation(summary = "Update an assessment", description = "Updates an existing assessment by ID. If not found, creates a new one.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assessment updated"),
        @ApiResponse(responseCode = "201", description = "Assessment created"),
        @ApiResponse(responseCode = "404", description = "Course or Instructor not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssessment(@Valid @RequestBody AssessmentDTO dto, @PathVariable Long id) {
        logger.info("Updating assessment with ID: {}", id);
        return assessmentService.updateAssessment(id, dto);
    }

    @Operation(summary = "Delete an assessment", description = "Deletes an assessment by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Assessment deleted"),
        @ApiResponse(responseCode = "404", description = "Assessment not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssessment(@PathVariable Long id) {
        logger.info("Deleting assessment with ID: {}", id);
        return assessmentService.deleteAssessment(id);
    }

    @GetMapping("/course/id/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN','STUDENT')")
    public PagedModel<EntityModel<AssessmentDTO>> getAssessmentsByCourseId(
            @PathVariable Long courseId,
            @PageableDefault(size = 5, sort = "createdAt") Pageable pageable) {
        return assessmentService.getAssessmentsByCourseId(courseId, pageable);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/stream/course/{courseId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AssessmentDTO> streamByCourse(@PathVariable String courseId) {
      return assessmentSink.asFlux()
                           .filter(dto -> dto.getCourseCode().equals(courseId));
    }
 }



