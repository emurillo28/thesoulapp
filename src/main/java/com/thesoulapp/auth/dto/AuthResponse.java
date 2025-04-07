package com.thesoulapp.auth.dto;

import com.thesoulapp.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
} 