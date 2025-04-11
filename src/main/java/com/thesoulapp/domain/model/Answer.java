package com.thesoulapp.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Answer extends BaseEntity { // Inherit BaseEntity if ID/timestamps are desired per answer

    // ID, createdAt, updatedAt inherited

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_assessment_id", nullable = false)
    @NotNull
    private UserAssessment userAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @NotNull
    private Question question;

    @Column(columnDefinition = "TEXT") // Store various answer types as text
    private String answerText;

    // Removed score, validationStatus, metadata etc. - move to UserAssessment or a separate analysis entity
    // private int score;
    // private String validationStatus;
    // private String metadata;

    // --- Constructor / Builder --- 
    // Ensure necessary fields are set via Builder or constructor

    // --- Setter for Relationship Management (package-private) ---
    void setUserAssessment(UserAssessment userAssessment) {
        this.userAssessment = userAssessment;
    }
    
    void setQuestion(Question question) {
        this.question = question;
    }
} 