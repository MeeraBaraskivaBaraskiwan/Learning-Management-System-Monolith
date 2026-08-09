package com.example.project.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Users", description = "Operations related to user management")
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Retrieve all users", description = "Fetch all users with pagination support")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    public CollectionModel<EntityModel<UserDTO>> getAllUsers(Pageable pageable) {
        logger.info("Fetching all users with pageable: {}", pageable);
        return userService.getAllUsers(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new user", description = "Add a new user to the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    public ResponseEntity<?> newUser(@Valid @RequestBody UserDTO newUserDTO) {
        logger.info("Creating a new user with email: {}", newUserDTO.getEmail());
        return userService.newUser(newUserDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Retrieve a user by ID", description = "Fetch a specific user by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    public EntityModel<UserDTO> getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a user", description = "Update the details of an existing user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO updatedUserDTO) {
        logger.info("Updating user with ID: {}", id);
        return userService.updateUser(id, updatedUserDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a user", description = "Remove a user from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        return userService.deleteUser(id);
    }
}