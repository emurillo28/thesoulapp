package com.thesoulapp.assessment.service;

import com.thesoulapp.assessment.model.Assessment;
import com.thesoulapp.assessment.repository.AssessmentRepository;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public Assessment createAssessment(Assessment assessment, User therapist) {
        // Validate therapist role
        if (therapist.getRole() != UserRole.THERAPIST) {
            throw new RuntimeException("Only therapists can create assessments");
        }

        // Set metadata
        assessment.setId(UUID.randomUUID().toString());
        assessment.setCreatedBy(therapist.getEmail());
        assessment.setCreatedAt(LocalDateTime.now());
        assessment.setActive(true);

        // Validate questions
        validateAssessment(assessment);

        // Save to database
        return assessmentRepository.save(assessment);
    }

    public Assessment getAssessment(String id) {
        return assessmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Assessment not found"));
    }

    public List<Assessment> getAssessmentsByTherapist(String therapistEmail) {
        return assessmentRepository.findByCreatedBy(therapistEmail);
    }

    public List<Assessment> getActiveAssessments() {
        return assessmentRepository.findByIsActiveTrue();
    }

    public Assessment updateAssessment(String id, Assessment updatedAssessment, User therapist) {
        Assessment existingAssessment = getAssessment(id);

        // Verify ownership
        if (!existingAssessment.getCreatedBy().equals(therapist.getEmail())) {
            throw new RuntimeException("Not authorized to update this assessment");
        }

        // Update fields
        existingAssessment.setTitle(updatedAssessment.getTitle());
        existingAssessment.setDescription(updatedAssessment.getDescription());
        existingAssessment.setQuestions(updatedAssessment.getQuestions());
        existingAssessment.setMetadata(updatedAssessment.getMetadata());

        // Validate and save
        validateAssessment(existingAssessment);
        return assessmentRepository.save(existingAssessment);
    }

    public void deactivateAssessment(String id, User therapist) {
        Assessment assessment = getAssessment(id);

        // Verify ownership
        if (!assessment.getCreatedBy().equals(therapist.getEmail())) {
            throw new RuntimeException("Not authorized to deactivate this assessment");
        }

        assessment.setActive(false);
        assessmentRepository.save(assessment);
    }

    private void validateAssessment(Assessment assessment) {
        if (assessment.getTitle() == null || assessment.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Assessment title is required");
        }

        if (assessment.getQuestions() == null || assessment.getQuestions().isEmpty()) {
            throw new RuntimeException("Assessment must have at least one question");
        }

        // Validate each question
        assessment.getQuestions().forEach(question -> {
            if (question.getQuestion() == null || question.getQuestion().trim().isEmpty()) {
                throw new RuntimeException("Question text is required");
            }
            
            if (question.getType() == null) {
                throw new RuntimeException("Question type is required");
            }

            // Validate options for multiple choice questions
            if (question.getType() == Assessment.QuestionType.MULTIPLE_CHOICE 
                && (question.getOptions() == null || question.getOptions().isEmpty())) {
                throw new RuntimeException("Multiple choice questions must have options");
            }

            // Validate scale questions
            if (question.getType() == Assessment.QuestionType.SCALE) {
                if (question.getMinValue() == null || question.getMaxValue() == null) {
                    throw new RuntimeException("Scale questions must have min and max values");
                }
                if (question.getMinValue() >= question.getMaxValue()) {
                    throw new RuntimeException("Scale max value must be greater than min value");
                }
            }
        });
    }
} 