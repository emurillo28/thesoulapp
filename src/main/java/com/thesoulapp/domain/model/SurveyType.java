package com.thesoulapp.domain.model;

public enum SurveyType {
    IMMEDIATE("Quick assessment for immediate matching"),
    MEDIUM("Detailed assessment for better matching"),
    DEEP("Comprehensive assessment for specialized care"),
    FOLLOW_UP("Progress tracking survey"),
    CUSTOM("Custom survey created by therapist");
    
    private final String description;
    
    SurveyType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isOnboardingType() {
        return this == IMMEDIATE || this == MEDIUM || this == DEEP;
    }
    
    public int getEstimatedDuration() {
        return switch (this) {
            case IMMEDIATE -> 5;
            case MEDIUM -> 15;
            case DEEP -> 30;
            case FOLLOW_UP -> 10;
            case CUSTOM -> 20;
        };
    }
} 