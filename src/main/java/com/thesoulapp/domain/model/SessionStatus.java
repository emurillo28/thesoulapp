package com.thesoulapp.domain.model;

public enum SessionStatus {
    SCHEDULED("Session is scheduled"),
    IN_PROGRESS("Session is in progress"),
    COMPLETED("Session has been completed"),
    CANCELLED("Session was cancelled"),
    RESCHEDULED("Session was rescheduled"),
    NO_SHOW("Patient did not attend"),
    WAITING("Waiting for session to start");
    
    private final String description;
    
    SessionStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isActive() {
        return this == SCHEDULED || 
               this == IN_PROGRESS || 
               this == WAITING;
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || 
               this == CANCELLED || 
               this == NO_SHOW;
    }
    
    public boolean canBeStarted() {
        return this == SCHEDULED || 
               this == WAITING;
    }
    
    public boolean canBeCancelled() {
        return this == SCHEDULED || 
               this == WAITING;
    }
    
    public boolean canBeRescheduled() {
        return this == SCHEDULED || 
               this == WAITING;
    }
    
    public boolean requiresPayment() {
        return this == COMPLETED || 
               this == NO_SHOW;
    }
} 