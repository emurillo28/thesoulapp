package com.thesoulapp.domain.model;

public enum UserStatus {
    PENDING,
    ACTIVE,
    SUSPENDED,
    DEACTIVATED,
    DELETED;
    
    public boolean canLogin() {
        return this == ACTIVE;
    }
    
    public boolean canAccessFeatures() {
        return this == ACTIVE;
    }
    
    public boolean isTerminal() {
        return this == DELETED;
    }
} 