package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;

@Getter
@Setter
@Builder
public class Answer {
    private UUID id;
    private UUID questionId;
    private UUID surveyId;
    private UUID userId;
    private String value;
    private int score;
    private LocalDateTime answeredAt;
    private String metadata;
    private boolean isSensitive;
    private String validationStatus;
    private String validationMessage;
    private String source;
    private String deviceInfo;
    private String location;
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_assessment_id", nullable = false)
    private UserAssessment userAssessment;

    // Domain methods
    public boolean isValid() {
        return validationStatus != null && 
               validationStatus.equals("VALID");
    }
    
    public void markAsValid() {
        this.validationStatus = "VALID";
    }
    
    public void markAsInvalid(String message) {
        this.validationStatus = "INVALID";
        this.validationMessage = message;
    }
    
    public void updateScore(int newScore) {
        this.score = newScore;
    }
    
    public boolean isFromMobile() {
        return source != null && 
               (source.equals("MOBILE_APP") || 
                source.equals("MOBILE_WEB"));
    }
    
    public boolean isFromWeb() {
        return source != null && 
               source.equals("WEB");
    }
    
    public boolean isFromAPI() {
        return source != null && 
               source.equals("API");
    }

    public UserAssessment getUserAssessment() {
        return userAssessment;
    }

    public void setUserAssessment(UserAssessment userAssessment) {
        this.userAssessment = userAssessment;
    }
} 