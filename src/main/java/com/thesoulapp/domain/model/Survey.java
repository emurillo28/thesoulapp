package com.thesoulapp.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "surveys")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Survey title cannot be blank")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private SurveyType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private SurveyStatus status = SurveyStatus.DRAFT; // Default to draft

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC") // Maintain question order
    private List<Question> questions = new ArrayList<>();

    @PositiveOrZero
    private int estimatedDurationMinutes;

    private String category;
    private boolean required = false;
    private LocalDateTime expiresAt;
    private String createdByUserId; // Link to user ID who created it
    private String version;

    // --- Domain State Query Methods ---
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isPublished() {
        return status == SurveyStatus.ACTIVE && !isExpired();
    }

    // --- State Mutating Methods (Called from Service Layer) ---
    public void publish() {
        if (this.status == SurveyStatus.DRAFT) { // Ensure can only publish from draft
            this.status = SurveyStatus.ACTIVE;
            // PreUpdate handles updatedAt
        }
    }
    
    public void archive() {
         this.status = SurveyStatus.ARCHIVED;
         // PreUpdate handles updatedAt
    }
    
    // --- Helper Methods for Relationship Management ---
    public void addQuestion(Question question) {
        questions.add(question);
        question.setSurvey(this);
        // Potentially set orderIndex here
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
        question.setSurvey(null);
    }

    // Domain methods
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