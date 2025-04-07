package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Question {
    private UUID id;
    private String text;
    private QuestionType type;
    private List<String> options;
    private boolean isRequired;
    private int order;
    private String category;
    private String subCategory;
    private int weight;
    private String validationRule;
    private String helpText;
    private List<String> tags;
    private String scoringRule;
    private int maxScore;
    private boolean isSensitive;
    private String skipLogic;
    private String displayLogic;
    
    // Domain methods
    public boolean isValidAnswer(String answer) {
        if (isRequired && (answer == null || answer.trim().isEmpty())) {
            return false;
        }
        
        // TODO: Implement validation based on validationRule
        return true;
    }
    
    public int calculateScore(String answer) {
        // TODO: Implement scoring based on scoringRule
        return 0;
    }
    
    public boolean shouldSkip(List<Answer> previousAnswers) {
        // TODO: Implement skip logic based on skipLogic field
        return false;
    }
    
    public boolean shouldDisplay(List<Answer> previousAnswers) {
        // TODO: Implement display logic based on displayLogic field
        return true;
    }
    
    public boolean isMultipleChoice() {
        return type == QuestionType.MULTIPLE_CHOICE || 
               type == QuestionType.CHECKBOX;
    }
    
    public boolean isTextBased() {
        return type == QuestionType.TEXT || 
               type == QuestionType.LONG_TEXT;
    }
} 