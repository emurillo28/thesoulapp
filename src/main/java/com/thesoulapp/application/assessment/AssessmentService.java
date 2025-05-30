package com.thesoulapp.application.assessment;

import com.thesoulapp.domain.assessment.Assessment;
import java.util.List;
import java.util.UUID;

public interface AssessmentService {
    Assessment createAssessment(String title, String description, UUID therapistId, String questionsJson);
    List<Assessment> listAssessments();
    Assessment approveAssessment(UUID assessmentId, UUID therapistId);
} 