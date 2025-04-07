package com.thesoulapp.domain.model;

public enum UserRole {
    PATIENT,
    THERAPIST,
    ADMIN;
    
    public boolean canAccessPatientData() {
        return this == THERAPIST || this == ADMIN;
    }
    
    public boolean canManageTherapists() {
        return this == ADMIN;
    }
    
    public boolean canManageContent() {
        return this == THERAPIST || this == ADMIN;
    }
    
    public boolean canManageSystem() {
        return this == ADMIN;
    }
} 