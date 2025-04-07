package com.thesoulapp.domain.model;

public enum AvatarRarity {
    COMMON("Common avatar"),
    UNCOMMON("Uncommon avatar"),
    RARE("Rare avatar"),
    EPIC("Epic avatar"),
    LEGENDARY("Legendary avatar");
    
    private final String description;
    
    AvatarRarity(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getDropRate() {
        return switch (this) {
            case COMMON -> 0.5;
            case UNCOMMON -> 0.3;
            case RARE -> 0.15;
            case EPIC -> 0.04;
            case LEGENDARY -> 0.01;
        };
    }
    
    public int getRequiredXP() {
        return switch (this) {
            case COMMON -> 0;
            case UNCOMMON -> 1000;
            case RARE -> 5000;
            case EPIC -> 15000;
            case LEGENDARY -> 30000;
        };
    }
    
    public int getRequiredLevel() {
        return switch (this) {
            case COMMON -> 1;
            case UNCOMMON -> 5;
            case RARE -> 15;
            case EPIC -> 30;
            case LEGENDARY -> 50;
        };
    }
    
    public boolean canBeTraded() {
        return this == RARE || 
               this == EPIC || 
               this == LEGENDARY;
    }
    
    public boolean canBeGifted() {
        return this != LEGENDARY;
    }
    
    public int getMaxUsageCount() {
        return switch (this) {
            case COMMON -> Integer.MAX_VALUE;
            case UNCOMMON -> 1000;
            case RARE -> 500;
            case EPIC -> 100;
            case LEGENDARY -> 50;
        };
    }
} 