package com.thesoulapp.user.controller;

import com.thesoulapp.user.dto.UserProfileDto;
import com.thesoulapp.user.dto.UpdateUserProfileDto;
import com.thesoulapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(Authentication authentication) {
        String email = authentication.getName(); // Assumes email is the principal name
        return ResponseEntity.ok(userService.getUserProfileByEmail(email));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateUserProfileDto updateDto) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.updateUserProfile(email, updateDto));
    }
    
    // TODO: Add endpoints for therapist view if needed later, e.g., /api/users/{userId}/profile
} 