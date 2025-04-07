package com.thesoulapp.assessment.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
public class SubmitAssessmentDto {
    // Map where key is Question ID and value is the answer
    // Answer format depends on question type (e.g., String for OPEN_TEXT, Integer for SCALE, selected option String for MULTIPLE_CHOICE)
    @NotEmpty(message = "Answers cannot be empty")
    private Map<Long, Object> answers;
} 