package com.thesoulapp.assessment.dto;

import com.thesoulapp.domain.model.QuestionType;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AssessmentStructureDto {
    private Long id;
    private String title;
    private String description;
    private List<QuestionDto> questions;
}

@Data
@Builder
class QuestionDto {
    private Long id;
    private String text;
    private QuestionType type; // MULTIPLE_CHOICE, SCALE, OPEN_TEXT
    private List<String> options; // Only for MULTIPLE_CHOICE
    private Integer scaleMin; // Only for SCALE
    private Integer scaleMax; // Only for SCALE
} 