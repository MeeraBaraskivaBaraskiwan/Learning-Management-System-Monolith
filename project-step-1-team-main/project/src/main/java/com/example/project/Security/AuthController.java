package com.example.project.Security;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.project.Exceptions.AlreadyExistsException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Notifications.EmailService;
import com.example.project.Users.Role;
import com.example.project.Users.RoleRepository;
import com.example.project.Users.User;
import com.example.project.Users.UserDTO;
import com.example.project.Users.UserMapper;
import com.example.project.Users.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and token management")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    public AuthController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService,
                          EmailService emailService,
                          RefreshTokenService refreshTokenService,
                          UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
        this.refreshTokenService = refreshTokenService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        logger.info("Registering a new user with email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("User with email {} already exists", request.getEmail());
            throw new AlreadyExistsException("User with email " + request.getEmail() + " already exists.");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> {
                    logger.error("Role with name {} not found", request.getRole());
                    return new ResourceNotFoundException("Role with name " + request.getRole() + " not found");
                });

        User user = new User(
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            passwordEncoder.encode(request.getPassword()),
            role
        );
        userRepository.save(user);
        logger.info("User registered successfully with email: {}", user.getEmail());

        emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
        logger.info("Welcome email sent to: {}", user.getEmail());

        emailService.sendAdminNotification("admin@example.com", "New user registered: " + user.getEmail());
        logger.info("Admin notified about new user registration: {}", user.getEmail());

        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "Login a user", description = "Authenticates a user and returns access, refresh tokens, user info, and instructorId")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        logger.info("Attempting login for user with email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate JWT access token
        String accessToken = jwtUtil.generateToken(userDetails);
        logger.info("Access token generated for user: {}", request.getEmail());

        // Load User entity (to build DTO and fetch instructor linkage)
        User userEntity = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> {
                logger.error("User not found in database with email: {}", request.getEmail());
                return new ResourceNotFoundException("User not found with email: " + request.getEmail());
            });

        // Map to DTO
        UserDTO userDto = userMapper.toDTO(userEntity);

        // Create / rotate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());
        logger.info("Refresh token generated for user: {}", request.getEmail());

        // Extract instructorId (assuming User has a @OneToOne Instructor instructor)
        Long instructorId = null;
        if (userEntity.getInstructor() != null) {
            instructorId = userEntity.getInstructor().getId();
        }

        return ResponseEntity.ok(
            new AuthenticationResponse(
                accessToken,
                refreshToken.getToken(),
                userDto,
                instructorId
            )
        );
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Refresh tokens", description = "Generates new access and refresh tokens using an existing refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String token = request.getRefreshToken();
        logger.info("Received refresh token request: {}", token);

        RefreshToken oldRefreshToken = refreshTokenService.verifyRefreshToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(oldRefreshToken.getUser().getEmail());

        String newAccessToken = jwtUtil.generateToken(userDetails);
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(oldRefreshToken);

        return ResponseEntity.ok(
            new AuthenticationResponse(newAccessToken, newRefreshToken.getToken(), null, null)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Logout a user", description = "Invalidates the user's refresh token and logs them out")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Logout successful"),
        @ApiResponse(responseCode = "401", description = "User not authenticated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        String email = authentication.getName();
        logger.info("Logging out user: {}", email);

        refreshTokenService.deleteByUserEmail(email);

        return ResponseEntity.noContent().build();
    }

    // ─── DTO Classes ─────────────────────────────────────────────────────────────

    @Data
    static class RegisterRequest {
        private String email;
        private String password;
        private String role;
        private String firstName;
        private String lastName;
    }

    @Data
    static class AuthenticationRequest {
        private String email;
        private String password;
    }

    @Data
    static class AuthenticationResponse {
        private final String accessToken;
        private final String refreshToken;
        private final UserDTO  userDto;
        private final Long     instructorId;

        public AuthenticationResponse(String accessToken, String refreshToken) {
            this.accessToken    = accessToken;
            this.refreshToken   = refreshToken;
            this.userDto        = null;
            this.instructorId   = null;
        }

        public AuthenticationResponse(
            String accessToken,
            String refreshToken,
            UserDTO userDto,
            Long instructorId
        ) {
            this.accessToken    = accessToken;
            this.refreshToken   = refreshToken;
            this.userDto        = userDto;
            this.instructorId   = instructorId;
        }
    }

    @Data
    static class RefreshTokenRequest {
        private String refreshToken;
    }
}
