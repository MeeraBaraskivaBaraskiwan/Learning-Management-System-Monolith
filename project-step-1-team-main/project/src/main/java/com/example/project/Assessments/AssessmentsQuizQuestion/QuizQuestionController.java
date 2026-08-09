package com.example.project.Assessments.AssessmentsQuizQuestion;

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

@Tag(name = "Quiz Questions", description = "Endpoints for managing quiz questions")
@RestController
@RequestMapping("/quiz-questions")
public class QuizQuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuizQuestionController.class);

    private final QuizQuestionService quizQuestionService;

    public QuizQuestionController(QuizQuestionService quizQuestionService) {
        this.quizQuestionService = quizQuestionService;
    }

    @Operation(summary = "Get all quiz questions", description = "Fetches a paginated list of all quiz questions.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved quiz questions")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<QuizQuestionDTO>> getAllQuizQuestions(
            @PageableDefault(size = 5, sort = "questionNumber") Pageable pageable) {
        logger.info("Fetching all quiz questions with pageable: {}", pageable);
        PagedModel<EntityModel<QuizQuestionDTO>> quizQuestions = quizQuestionService.getAllQuizQuestions(pageable);
        logger.debug("Fetched {} quiz questions", quizQuestions.getContent().size());
        return quizQuestions;
    }

    @Operation(summary = "Get quiz question by ID", description = "Fetches a specific quiz question by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz question found"),
        @ApiResponse(responseCode = "404", description = "Quiz question not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<QuizQuestionDTO> getQuizQuestionById(@PathVariable Long id) {
        logger.info("Fetching quiz question by ID: {}", id);
        EntityModel<QuizQuestionDTO> quizQuestion = quizQuestionService.getQuizQuestionById(id);
        logger.debug("Fetched quiz question: {}", quizQuestion);
        return quizQuestion;
    }

    @Operation(summary = "Get quiz questions by quiz ID", description = "Fetches all questions belonging to a specific quiz.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Questions found for quiz"),
        @ApiResponse(responseCode = "404", description = "No questions found for the quiz")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN', 'STUDENT')")
    @GetMapping("/quiz/{quizId}")
    public PagedModel<EntityModel<QuizQuestionDTO>> getQuizQuestionsByQuizId(
            @PathVariable Long quizId, @PageableDefault(size = 5) Pageable pageable) {
        logger.info("Fetching quiz questions for quiz ID: {}", quizId);
        PagedModel<EntityModel<QuizQuestionDTO>> quizQuestions = quizQuestionService.getQuizQuestionsByQuizId(quizId, pageable);
        logger.debug("Fetched {} quiz questions for quiz ID: {}", quizQuestions.getContent().size(), quizId);
        return quizQuestions;
    }

    @Operation(summary = "Create a new quiz question", description = "Adds a new question to a quiz.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Quiz question created"),
        @ApiResponse(responseCode = "404", description = "Quiz not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PostMapping
    public ResponseEntity<?> createQuizQuestion(@Valid @RequestBody QuizQuestionDTO dto) {
        logger.info("Creating a new quiz question for quiz ID: {}", dto.getQuizId());
        ResponseEntity<?> response = quizQuestionService.createQuizQuestion(dto);
        logger.debug("Created quiz question: {}", response);
        return response;
    }

    @Operation(summary = "Update a quiz question", description = "Updates an existing quiz question. If not found, creates a new one.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz question updated"),
        @ApiResponse(responseCode = "201", description = "Quiz question created"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuizQuestion(@PathVariable Long id, @Valid @RequestBody QuizQuestionDTO dto) {
        logger.info("Updating quiz question with ID: {}", id);
        ResponseEntity<?> response = quizQuestionService.updateQuizQuestion(id, dto);
        logger.debug("Updated quiz question: {}", response);
        return response;
    }

    @Operation(summary = "Delete a quiz question", description = "Deletes a quiz question by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Quiz question deleted"),
        @ApiResponse(responseCode = "404", description = "Quiz question not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuizQuestion(@PathVariable Long id) {
        logger.info("Deleting quiz question with ID: {}", id);
        ResponseEntity<?> response = quizQuestionService.deleteQuizQuestion(id);
        logger.debug("Deleted quiz question with ID: {}", id);
        return response;
    }
}

