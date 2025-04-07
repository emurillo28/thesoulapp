package com.thesoulapp.domain.model;

public enum ReminderType {
    APPOINTMENT("Therapy session reminder"),
    MEDICATION("Medication reminder"),
    SURVEY("Survey completion reminder"),
    EXERCISE("Therapeutic exercise reminder"),
    JOURNAL("Journal entry reminder"),
    CHECK_IN("Wellness check-in reminder"),
    PAYMENT("Payment due reminder"),
    FOLLOW_UP("Follow-up appointment reminder"),
    CUSTOM("Custom reminder");
    
    private final String description;
    
    ReminderType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isHealthRelated() {
        return this == MEDICATION || 
               this == EXERCISE || 
               this == CHECK_IN;
    }
    
    public boolean isAppointmentRelated() {
        return this == APPOINTMENT || 
               this == FOLLOW_UP;
    }
    
    public boolean isActivityRelated() {
        return this == SURVEY || 
               this == JOURNAL;
    }
    
    public int getDefaultPriority() {
        return switch (this) {
            case MEDICATION -> 10;
            case APPOINTMENT -> 9;
            case FOLLOW_UP -> 8;
            case CHECK_IN -> 7;
            case EXERCISE -> 6;
            case SURVEY -> 5;
            case JOURNAL -> 4;
            case PAYMENT -> 3;
            case CUSTOM -> 2;
        };
    }
} 