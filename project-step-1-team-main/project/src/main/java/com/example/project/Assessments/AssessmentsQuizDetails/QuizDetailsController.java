package com.example.project.Assessments.AssessmentsQuizDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;

@Tag(name = "Quiz Details", description = "Endpoints for managing quiz details")
@RestController
@RequestMapping("/quiz-details")
public class QuizDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(QuizDetailsController.class);

    private final QuizDetailsService quizDetailsService;

    public QuizDetailsController(QuizDetailsService quizDetailsService) {
        this.quizDetailsService = quizDetailsService;
    }

    @Operation(summary = "Get all quiz details", description = "Fetches a paginated list of all quiz details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quiz details")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<QuizDetailsDTO>> getAllQuizDetails(
            @PageableDefault(size = 5, sort = "openTime") Pageable pageable) {
        logger.info("Fetching all quiz details with pageable: {}", pageable);
        PagedModel<EntityModel<QuizDetailsDTO>> quizDetails = quizDetailsService.getAllQuizDetails(pageable);
        logger.debug("Fetched {} quiz details", quizDetails.getContent().size());
        return quizDetails;
    }

    @Operation(summary = "Get quiz details by ID", description = "Fetches specific quiz details by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz details found"),
        @ApiResponse(responseCode = "404", description = "Quiz details not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<QuizDetailsDTO> getQuizDetailsById(@PathVariable Long id) {
        logger.info("Fetching quiz details by ID: {}", id);
        EntityModel<QuizDetailsDTO> quizDetails = quizDetailsService.getQuizDetailsById(id);
        logger.debug("Fetched quiz details: {}", quizDetails);
        return quizDetails;
    }

    @Operation(summary = "Get quiz details by assessment ID", description = "Fetches quiz details for a specific assessment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz details found"),
        @ApiResponse(responseCode = "404", description = "Quiz details not found for the assessment")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/assessment/{assessmentId}")
    public EntityModel<QuizDetailsDTO> getQuizDetailsByAssessmentId(@PathVariable Long assessmentId) {
        logger.info("Fetching quiz details by assessment ID: {}", assessmentId);
        EntityModel<QuizDetailsDTO> quizDetails = quizDetailsService.getQuizDetailsByAssessmentId(assessmentId);
        logger.debug("Fetched quiz details for assessment ID {}: {}", assessmentId, quizDetails);
        return quizDetails;
    }

    @Operation(summary = "Create quiz details", description = "Creates new quiz details for a given assessment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz details created"),
        @ApiResponse(responseCode = "404", description = "Assessment not found or invalid"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createQuizDetails(@Valid @RequestBody QuizDetailsDTO quizDetailsDTO) {
        logger.info("Creating quiz details for assessment ID: {}", quizDetailsDTO.getAssessmentId());
        ResponseEntity<?> response = quizDetailsService.createQuizDetails(quizDetailsDTO);
        logger.debug("Created quiz details: {}", response);
        return response;
    }

    @Operation(summary = "Update quiz details", description = "Updates existing quiz details or creates new ones if not found.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz details updated"),
        @ApiResponse(responseCode = "201", description = "Quiz details created"),
        @ApiResponse(responseCode = "404", description = "Assessment not found or invalid")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuizDetails(@PathVariable Long id, @Valid @RequestBody QuizDetailsDTO quizDetailsDTO) {
        logger.info("Updating quiz details with ID: {}", id);
        ResponseEntity<?> response = quizDetailsService.updateQuizDetails(id, quizDetailsDTO);
        logger.debug("Updated quiz details: {}", response);
        return response;
    }

    @Operation(summary = "Delete quiz details", description = "Deletes quiz details by ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz details deleted"),
        @ApiResponse(responseCode = "404", description = "Quiz details not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuizDetails(@PathVariable Long id) {
        logger.info("Deleting quiz details with ID: {}", id);
        ResponseEntity<?> response = quizDetailsService.deleteQuizDetails(id);
        logger.debug("Deleted quiz details with ID: {}", id);
        return response;
    }
}
