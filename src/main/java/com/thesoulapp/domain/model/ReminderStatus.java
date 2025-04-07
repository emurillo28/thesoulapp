package com.thesoulapp.domain.model;

public enum ReminderStatus {
    PENDING("Reminder is scheduled"),
    SENT("Reminder has been sent"),
    FAILED("Reminder failed to send"),
    CANCELLED("Reminder was cancelled"),
    COMPLETED("Reminder action was completed");
    
    private final String description;
    
    ReminderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isTerminal() {
        return this == COMPLETED || 
               this == CANCELLED;
    }
    
    public boolean canBeRetried() {
        return this == FAILED;
    }
    
    public boolean isActive() {
        return this == PENDING;
    }
    
    public boolean isSuccessful() {
        return this == SENT || 
               this == COMPLETED;
    }
} 