package com.thesoulapp.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileDto {
    private Long id;
    private String email;
    private String name; // Assuming a name field exists
    private String role; // e.g., ROLE_USER, ROLE_THERAPIST
    private String subscriptionTier; // Basic, Acompañada, Premium
    private Integer points; // Gamification points
    // Add other relevant profile fields (e.g., avatarUrl)
} 