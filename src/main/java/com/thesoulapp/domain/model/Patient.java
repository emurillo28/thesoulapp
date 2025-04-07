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
public class Patient {
    private UUID id;
    private UUID userId;
    private String emergencyContact;
    private String emergencyPhone;
    private String insuranceProvider;
    private String insuranceId;
    private List<String> allergies;
    private List<String> medications;
    private String primaryLanguage;
    private String preferredContactMethod;
    private boolean hasConsent;
    private LocalDateTime consentDate;
    private String consentDocumentUrl;
    private int xpPoints;
    private int level;
    private List<String> unlockedAvatars;
    private List<String> unlockedThemes;
    private LocalDateTime lastAssessmentDate;
    private String currentTherapistId;
    private List<String> previousTherapists;
    private LocalDateTime therapyStartDate;
    private String therapyGoals;
    private String preferredTherapyType;
    private String preferredSchedule;
    private String preferredLocation;
    
    // Domain methods
    public void addXP(int points) {
        this.xpPoints += points;
        updateLevel();
    }
    
    private void updateLevel() {
        // Simple level calculation: every 1000 XP = 1 level
        this.level = (this.xpPoints / 1000) + 1;
    }
    
    public void unlockAvatar(String avatarId) {
        if (!unlockedAvatars.contains(avatarId)) {
            unlockedAvatars.add(avatarId);
        }
    }
    
    public void unlockTheme(String themeId) {
        if (!unlockedThemes.contains(themeId)) {
            unlockedThemes.add(themeId);
        }
    }
    
    public void giveConsent(String documentUrl) {
        this.hasConsent = true;
        this.consentDate = LocalDateTime.now();
        this.consentDocumentUrl = documentUrl;
    }
    
    public void assignTherapist(String therapistId) {
        if (currentTherapistId != null) {
            previousTherapists.add(currentTherapistId);
        }
        this.currentTherapistId = therapistId;
        this.therapyStartDate = LocalDateTime.now();
    }
    
    public boolean hasActiveTherapist() {
        return currentTherapistId != null;
    }
    
    public void updateTherapyGoals(String goals) {
        this.therapyGoals = goals;
    }
    
    public void updatePreferences(String therapyType, String schedule, String location) {
        this.preferredTherapyType = therapyType;
        this.preferredSchedule = schedule;
        this.preferredLocation = location;
    }
} 