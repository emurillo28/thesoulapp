package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Column;

@Getter
@Setter
@Builder
public class User {
    private UUID id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private UserRole role;
    private UserStatus status;
    private String phoneNumber;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String avatarUrl;
    private String preferredLanguage;
    private String timezone;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    // Domain methods
    public void verifyEmail() {
        this.emailVerified = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void verifyPhone() {
        this.phoneVerified = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
    
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }
    
    public boolean isFullyVerified() {
        return emailVerified && phoneVerified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
} 