package com.thesoulapp.user.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateUserProfileDto {
    // Add fields that the user can update, e.g.:
    @NotBlank(message = "Name cannot be blank")
    private String name;
    // Add other updatable fields like avatar preferences, etc.
} 