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
public class Survey {
    private UUID id;
    private String title;
    private String description;
    private SurveyType type;
    private SurveyStatus status;
    private List<Question> questions;
    private int estimatedDuration;
    private String category;
    private boolean isRequired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private String createdBy;
    private String version;
    private List<String> tags;
    private int minimumScore;
    private int maximumScore;
    private String scoringAlgorithm;
    
    // Domain methods
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isActive() {
        return status == SurveyStatus.ACTIVE && !isExpired();
    }
    
    public void activate() {
        this.status = SurveyStatus.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void deactivate() {
        this.status = SurveyStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void archive() {
        this.status = SurveyStatus.ARCHIVED;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateVersion(String newVersion) {
        this.version = newVersion;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean hasValidScoreRange() {
        return minimumScore <= maximumScore;
    }
    
    public int calculateScore(List<Answer> answers) {
        // TODO: Implement scoring algorithm based on scoringAlgorithm field
        return 0;
    }
} 