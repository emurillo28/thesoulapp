package com.thesoulapp.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_assessments")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserAssessment extends BaseEntity {

    // ID, createdAt, updatedAt inherited

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @NotNull
    private Survey survey;

    @OneToMany(mappedBy = "userAssessment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime completedAt; // Consider renaming from createdAt if inherited?

    // Potential field for calculated score/result
    private Integer calculatedScore;
    private String resultSummary; // Or link to a separate result entity

    // --- Helper Methods for Relationship Management ---
    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setUserAssessment(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setUserAssessment(null);
    }
    
    // --- State Mutating Methods (Called from Service Layer) ---
    public void recordCompletion(LocalDateTime completionTime) {
        this.completedAt = completionTime;
        // PreUpdate handles updatedAt
    }
    
    public void recordResult(Integer score, String summary) {
        this.calculatedScore = score;
        this.resultSummary = summary;
        // PreUpdate handles updatedAt
    }
    
    // Setters for JPA relationship management (package-private)
    void setUser(User user) {
        this.user = user;
    }
    
    void setSurvey(Survey survey) {
        this.survey = survey;
    }
} 