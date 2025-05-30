package com.thesoulapp.application.assessment.impl;

import com.thesoulapp.application.assessment.AssessmentService;
import com.thesoulapp.domain.assessment.Assessment;
import com.thesoulapp.domain.assessment.AssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class AssessmentServiceImpl implements AssessmentService {
    private final AssessmentRepository assessmentRepository;

    @Autowired
    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Override
    public Assessment createAssessment(String title, String description, UUID therapistId, String questionsJson) {
        Assessment assessment = Assessment.builder()
                .title(title)
                .description(description)
                .createdBy(therapistId)
                .approvedBy(List.of())
                .questions(questionsJson)
                .build();
        return assessmentRepository.save(assessment);
    }

    @Override
    public List<Assessment> listAssessments() {
        return assessmentRepository.findAll();
    }

    @Override
    public Assessment approveAssessment(UUID assessmentId, UUID therapistId) {
        Assessment assessment = assessmentRepository.findById(assessmentId).orElseThrow();
        List<UUID> approvedBy = assessment.getApprovedBy();
        if (!approvedBy.contains(therapistId)) {
            approvedBy.add(therapistId);
            assessment.setApprovedBy(approvedBy);
            return assessmentRepository.save(assessment);
        }
        return assessment;
    }
} 