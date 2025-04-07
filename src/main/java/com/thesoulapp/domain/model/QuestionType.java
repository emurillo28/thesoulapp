package com.thesoulapp.domain.model;

public enum QuestionType {
    TEXT("Short text answer"),
    LONG_TEXT("Long text answer"),
    MULTIPLE_CHOICE("Single choice from options"),
    CHECKBOX("Multiple choices from options"),
    RATING("Numeric rating scale"),
    LIKERT("Likert scale"),
    DATE("Date picker"),
    TIME("Time picker"),
    DATETIME("Date and time picker"),
    FILE("File upload"),
    IMAGE("Image upload"),
    VIDEO("Video upload"),
    AUDIO("Audio upload"),
    LOCATION("Location picker"),
    MATRIX("Matrix of questions"),
    RANKING("Rank items in order");
    
    private final String description;
    
    QuestionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean requiresOptions() {
        return this == MULTIPLE_CHOICE || 
               this == CHECKBOX || 
               this == RANKING;
    }
    
    public boolean isMediaType() {
        return this == FILE || 
               this == IMAGE || 
               this == VIDEO || 
               this == AUDIO;
    }
    
    public boolean isDateTimeType() {
        return this == DATE || 
               this == TIME || 
               this == DATETIME;
    }
} 