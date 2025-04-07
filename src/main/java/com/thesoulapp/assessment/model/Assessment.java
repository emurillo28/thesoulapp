package com.thesoulapp.assessment.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@DynamoDBTable(tableName = "Assessments")
public class Assessment {
    
    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String title;

    @DynamoDBAttribute
    private String description;

    @DynamoDBAttribute
    private String createdBy; // Therapist's email

    @DynamoDBAttribute
    private LocalDateTime createdAt;

    @DynamoDBAttribute
    private boolean isActive;

    @DynamoDBAttribute
    private List<AssessmentQuestion> questions;

    @DynamoDBAttribute
    private Map<String, Object> metadata; // Flexible metadata for future extensions

    @Data
    public static class AssessmentQuestion {
        private String id;
        private String question;
        private QuestionType type;
        private List<String> options; // For multiple choice questions
        private boolean required;
        private Integer minValue; // For scale questions
        private Integer maxValue; // For scale questions
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