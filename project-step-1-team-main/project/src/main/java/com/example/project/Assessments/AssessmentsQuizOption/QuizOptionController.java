package com.example.project.Assessments.AssessmentsQuizOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
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

@Tag(name = "Quiz Options", description = "Endpoints for managing quiz options")
@RestController
@RequestMapping("/quiz-options")
public class QuizOptionController {

    private static final Logger logger = LoggerFactory.getLogger(QuizOptionController.class);

    private final QuizOptionService quizOptionService;

    public QuizOptionController(QuizOptionService quizOptionService) {
        this.quizOptionService = quizOptionService;
    }

    @Operation(summary = "Get all quiz options", description = "Fetches a paginated list of all quiz options.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quiz options")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<QuizOptionDTO>> getAllQuizOptions(
            @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching all quiz options with pagination: {}", pageable);
        return quizOptionService.getAllQuizOptions(pageable);
    }

    @Operation(summary = "Get quiz option by ID", description = "Fetches a specific quiz option by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz option found"),
        @ApiResponse(responseCode = "404", description = "Quiz option not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<QuizOptionDTO> getQuizOptionById(@PathVariable Long id) {
        logger.info("Fetching quiz option by ID: {}", id);
        return quizOptionService.getQuizOptionById(id);
    }

    @Operation(summary = "Get quiz options by question ID", description = "Fetches all options for a specific quiz question.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Options found for question"),
        @ApiResponse(responseCode = "404", description = "No options found for the question")
    })
    @PreAuthorize("hasAnyRole('STUDENT', 'INSTRUCTOR', 'ADMIN')")
    @GetMapping("/question/{questionId}")
    public PagedModel<EntityModel<QuizOptionDTO>> getQuizOptionsByQuestionId(
            @PathVariable Long questionId, @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching quiz options by question ID: {}", questionId);
        return quizOptionService.getQuizOptionsByQuestionId(questionId, pageable);
    }

    @Operation(summary = "Create a new quiz option", description = "Adds a new option to a quiz question.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz option created"),
        @ApiResponse(responseCode = "404", description = "Quiz question not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createQuizOption(@Valid @RequestBody QuizOptionDTO dto) {
        logger.info("Creating new quiz option for question ID: {}", dto.getQuestionId());
        return quizOptionService.createQuizOption(dto);
    }

    @Operation(summary = "Update a quiz option", description = "Updates an existing quiz option. If not found, creates a new one.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz option updated"),
        @ApiResponse(responseCode = "201", description = "Quiz option created"),
        @ApiResponse(responseCode = "404", description = "Quiz question not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuizOption(@PathVariable Long id, @Valid @RequestBody QuizOptionDTO dto) {
        logger.info("Updating quiz option with ID: {}", id);
        return quizOptionService.updateQuizOption(id, dto);
    }

    @Operation(summary = "Delete a quiz option", description = "Deletes a quiz option by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz option deleted"),
        @ApiResponse(responseCode = "404", description = "Quiz option not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuizOption(@PathVariable Long id) {
        logger.info("Deleting quiz option with ID: {}", id);
        return quizOptionService.deleteQuizOption(id);
    }
}
