package com.thesoulapp.domain.model;

public enum SurveyStatus {
    DRAFT("Survey is being created"),
    ACTIVE("Survey is available for use"),
    INACTIVE("Survey is temporarily unavailable"),
    ARCHIVED("Survey is no longer in use");
    
    private final String description;
    
    SurveyStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean canBeEdited() {
        return this == DRAFT;
    }
    
    public boolean canBeTaken() {
        return this == ACTIVE;
    }
    
    public boolean canBeArchived() {
        return this == ACTIVE || this == INACTIVE;
    }
} 