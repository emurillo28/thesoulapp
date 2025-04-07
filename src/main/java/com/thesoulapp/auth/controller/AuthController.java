package com.thesoulapp.auth.controller;

import com.thesoulapp.auth.dto.AuthResponse;
import com.thesoulapp.auth.dto.LoginRequest;
import com.thesoulapp.auth.dto.SignUpRequest;
import com.thesoulapp.auth.dto.ConfirmSignUpRequest;
import com.thesoulapp.auth.dto.ConfirmPasswordResetRequest;
import com.thesoulapp.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/confirm-signup")
    public ResponseEntity<AuthResponse> confirmSignUp(@Valid @RequestBody ConfirmSignUpRequest request) {
        return ResponseEntity.ok(authService.confirmSignUp(request.getEmail(), request.getConfirmationCode()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String email) {
        authService.resetPassword(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm-reset-password")
    public ResponseEntity<Void> confirmResetPassword(@Valid @RequestBody ConfirmPasswordResetRequest request) {
        authService.confirmResetPassword(
            request.getEmail(), 
            request.getConfirmationCode(), 
            request.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
} 