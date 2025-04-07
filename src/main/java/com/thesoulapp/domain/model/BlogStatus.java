package com.thesoulapp.domain.model;

public enum BlogStatus {
    DRAFT("Post is in draft"),
    PUBLISHED("Post is published"),
    SCHEDULED("Post is scheduled for publishing"),
    ARCHIVED("Post is archived"),
    DELETED("Post is deleted");
    
    private final String description;
    
    BlogStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isVisible() {
        return this == PUBLISHED;
    }
    
    public boolean isEditable() {
        return this == DRAFT || 
               this == SCHEDULED;
    }
    
    public boolean isTerminal() {
        return this == DELETED;
    }
    
    public boolean canBePublished() {
        return this == DRAFT || 
               this == SCHEDULED;
    }
    
    public boolean canBeScheduled() {
        return this == DRAFT;
    }
    
    public boolean canBeArchived() {
        return this == PUBLISHED;
    }
} 