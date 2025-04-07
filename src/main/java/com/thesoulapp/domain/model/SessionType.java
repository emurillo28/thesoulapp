package com.thesoulapp.domain.model;

public enum SessionType {
    INDIVIDUAL("One-on-one therapy session"),
    GROUP("Group therapy session"),
    COUPLE("Couples therapy session"),
    FAMILY("Family therapy session"),
    WORKSHOP("Therapeutic workshop"),
    ASSESSMENT("Initial assessment session"),
    FOLLOW_UP("Follow-up session"),
    CRISIS("Crisis intervention session"),
    CONSULTATION("Professional consultation");
    
    private final String description;
    
    SessionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isGroupSession() {
        return this == GROUP || 
               this == COUPLE || 
               this == FAMILY || 
               this == WORKSHOP;
    }
    
    public boolean isIndividualSession() {
        return this == INDIVIDUAL || 
               this == ASSESSMENT || 
               this == FOLLOW_UP || 
               this == CRISIS || 
               this == CONSULTATION;
    }
    
    public boolean isEmergency() {
        return this == CRISIS;
    }
    
    public int getDefaultDuration() {
        return switch (this) {
            case ASSESSMENT -> 90;
            case CRISIS -> 60;
            case WORKSHOP -> 120;
            case GROUP, COUPLE, FAMILY -> 90;
            default -> 50;
        };
    }
    
    public boolean requiresSpecialist() {
        return this == CRISIS || 
               this == ASSESSMENT;
    }
} 