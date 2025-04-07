package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Avatar {
    private UUID id;
    private String name;
    private String description;
    private AvatarType type;
    private AvatarRarity rarity;
    private int requiredLevel;
    private int requiredXP;
    private List<String> requiredAchievements;
    private String imageUrl;
    private String animationUrl;
    private String soundUrl;
    private List<String> tags;
    private boolean isActive;
    private LocalDateTime unlockedAt;
    private LocalDateTime expiresAt;
    private String customData;
    private int usageCount;
    private int maxUsageCount;
    private boolean isLimitedEdition;
    private int editionNumber;
    private int totalEditions;
    private String creator;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Domain methods
    public boolean canBeUnlocked(int userLevel, int userXP, List<String> userAchievements) {
        if (userLevel < requiredLevel) {
            return false;
        }
        
        if (userXP < requiredXP) {
            return false;
        }
        
        for (String achievement : requiredAchievements) {
            if (!userAchievements.contains(achievement)) {
                return false;
            }
        }
        
        return true;
    }
    
    public void unlock() {
        this.isActive = true;
        this.unlockedAt = LocalDateTime.now();
    }
    
    public void lock() {
        this.isActive = false;
    }
    
    public boolean isExpired() {
        return expiresAt != null && 
               LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isLimited() {
        return isLimitedEdition && 
               usageCount >= maxUsageCount;
    }
    
    public void incrementUsage() {
        if (!isLimited() && isActive) {
            this.usageCount++;
        }
    }
    
    public boolean isRare() {
        return rarity == AvatarRarity.RARE || 
               rarity == AvatarRarity.EPIC || 
               rarity == AvatarRarity.LEGENDARY;
    }
    
    public boolean isEpic() {
        return rarity == AvatarRarity.EPIC || 
               rarity == AvatarRarity.LEGENDARY;
    }
    
    public boolean isLegendary() {
        return rarity == AvatarRarity.LEGENDARY;
    }
    
    public int getValue() {
        return switch (rarity) {
            case COMMON -> 100;
            case UNCOMMON -> 250;
            case RARE -> 500;
            case EPIC -> 1000;
            case LEGENDARY -> 2500;
        };
    }
} 