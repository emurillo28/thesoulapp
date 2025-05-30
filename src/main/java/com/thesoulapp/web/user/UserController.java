package com.thesoulapp.web.user;

import com.thesoulapp.application.user.UserService;
import com.thesoulapp.domain.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.Builder;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping
@Tag(name = "User Management", description = "Endpoints for user registration, login, and profile retrieval")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user")
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User user = userService.register(request.email(), request.password(), request.role(), request.profile());
        return ResponseEntity.ok(new UserResponse(user.getId().toString(), user.getEmail(), user.getRole().name(), user.getProfile()));
    }

    @Operation(summary = "Login and receive JWT token")
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.email(), request.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(summary = "Get current user profile (JWT required)")
    @GetMapping("/users/me")
    public ResponseEntity<?> me(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getProfile(email);
        return ResponseEntity.ok(new UserResponse(user.getId().toString(), user.getEmail(), user.getRole().name(), user.getProfile()));
    }

    @Builder
    public record SignupRequest(String email, String password, User.Role role, String profile) {}
    @Builder
    public record LoginRequest(String email, String password) {}
    @Builder
    public record LoginResponse(String token) {}
    @Builder
    public record UserResponse(String id, String email, String role, String profile) {}
} 