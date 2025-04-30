package com.thesoulapp.assessment.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "assessments")
public class Assessment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_by", nullable = false)
    private String createdBy; // Therapist's email

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "assessment_id")
    private List<AssessmentQuestion> questions;

    @ElementCollection
    @CollectionTable(name = "assessment_metadata", 
                    joinColumns = @JoinColumn(name = "assessment_id"))
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> metadata; // Flexible metadata for future extensions

    @Entity
    @Table(name = "assessment_questions")
    @Data
    public static class AssessmentQuestion {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @Column(nullable = false, columnDefinition = "TEXT")
        private String question;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private QuestionType type;

        @ElementCollection
        @CollectionTable(name = "question_options", 
                        joinColumns = @JoinColumn(name = "question_id"))
        @Column(name = "option_value")
        private List<String> options; // For multiple choice questions

        @Column(nullable = false)
        private boolean required;

        @Column(name = "min_value")
        private Integer minValue; // For scale questions

        @Column(name = "max_value")
        private Integer maxValue; // For scale questions

        @Column(name = "help_text", columnDefinition = "TEXT")
        private String helpText;
    }

    public enum QuestionType {
        TEXT,
        MULTIPLE_CHOICE,
        SCALE,
        YES_NO,
        MOOD_SCALE,
        OPEN_TEXT
    }
} 