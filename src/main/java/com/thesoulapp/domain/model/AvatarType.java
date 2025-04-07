package com.thesoulapp.domain.model;

public enum AvatarType {
    CHARACTER("Human or creature character"),
    ANIMAL("Animal representation"),
    ABSTRACT("Abstract design"),
    EMOJI("Emoji or emoticon"),
    BADGE("Achievement badge"),
    FRAME("Profile frame"),
    BACKGROUND("Profile background"),
    EFFECT("Visual effect"),
    SOUND("Sound effect"),
    ANIMATION("Animated effect");
    
    private final String description;
    
    AvatarType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isVisual() {
        return this == CHARACTER || 
               this == ANIMAL || 
               this == ABSTRACT || 
               this == EMOJI || 
               this == BADGE || 
               this == FRAME || 
               this == BACKGROUND || 
               this == EFFECT || 
               this == ANIMATION;
    }
    
    public boolean isAudio() {
        return this == SOUND;
    }
    
    public boolean isAnimated() {
        return this == ANIMATION || 
               this == EFFECT;
    }
    
    public boolean isStatic() {
        return this == CHARACTER || 
               this == ANIMAL || 
               this == ABSTRACT || 
               this == EMOJI || 
               this == BADGE || 
               this == FRAME || 
               this == BACKGROUND;
    }
    
    public int getDefaultRequiredLevel() {
        return switch (this) {
            case CHARACTER, ANIMAL -> 1;
            case ABSTRACT, EMOJI -> 5;
            case BADGE, FRAME -> 10;
            case BACKGROUND -> 15;
            case EFFECT -> 20;
            case SOUND -> 25;
            case ANIMATION -> 30;
        };
    }
} 