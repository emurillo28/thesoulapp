package com.thesoulapp.domain.model;

public enum BlogCategory {
    MENTAL_HEALTH("Mental health articles"),
    WELLNESS("Wellness and lifestyle"),
    THERAPY("Therapy techniques"),
    SELF_CARE("Self-care tips"),
    RELATIONSHIPS("Relationship advice"),
    ANXIETY("Anxiety management"),
    DEPRESSION("Depression support"),
    STRESS("Stress management"),
    MINDFULNESS("Mindfulness practices"),
    PARENTING("Parenting guidance"),
    WORK_LIFE("Work-life balance"),
    SLEEP("Sleep hygiene"),
    NUTRITION("Mental health nutrition"),
    EXERCISE("Exercise and mental health"),
    COMMUNITY("Community stories"),
    RESEARCH("Research updates"),
    PROFESSIONAL("Professional insights"),
    RESOURCES("Resource guides"),
    EVENTS("Event announcements"),
    NEWS("Mental health news");
    
    private final String description;
    
    BlogCategory(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isEducational() {
        return this == MENTAL_HEALTH || 
               this == THERAPY || 
               this == RESEARCH || 
               this == PROFESSIONAL;
    }
    
    public boolean isSupportive() {
        return this == SELF_CARE || 
               this == WELLNESS || 
               this == RESOURCES;
    }
    
    public boolean isConditionSpecific() {
        return this == ANXIETY || 
               this == DEPRESSION || 
               this == STRESS;
    }
    
    public boolean isLifestyle() {
        return this == RELATIONSHIPS || 
               this == PARENTING || 
               this == WORK_LIFE || 
               this == SLEEP || 
               this == NUTRITION || 
               this == EXERCISE;
    }
    
    public boolean isCommunity() {
        return this == COMMUNITY || 
               this == EVENTS || 
               this == NEWS;
    }
    
    public String getDefaultTags() {
        return switch (this) {
            case MENTAL_HEALTH -> "mental-health,education,awareness";
            case WELLNESS -> "wellness,lifestyle,health";
            case THERAPY -> "therapy,techniques,professional";
            case SELF_CARE -> "self-care,tips,wellbeing";
            case RELATIONSHIPS -> "relationships,communication,connection";
            case ANXIETY -> "anxiety,mental-health,support";
            case DEPRESSION -> "depression,mental-health,support";
            case STRESS -> "stress,management,coping";
            case MINDFULNESS -> "mindfulness,meditation,awareness";
            case PARENTING -> "parenting,family,children";
            case WORK_LIFE -> "work,balance,productivity";
            case SLEEP -> "sleep,rest,health";
            case NUTRITION -> "nutrition,diet,health";
            case EXERCISE -> "exercise,fitness,mental-health";
            case COMMUNITY -> "community,support,stories";
            case RESEARCH -> "research,science,updates";
            case PROFESSIONAL -> "professional,insights,expertise";
            case RESOURCES -> "resources,guides,help";
            case EVENTS -> "events,community,activities";
            case NEWS -> "news,updates,information";
        };
    }
} 