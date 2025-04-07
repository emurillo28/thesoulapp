package com.thesoulapp.assessment.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.thesoulapp.assessment.model.Assessment;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final DynamoDBMapper dynamoDBMapper;

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

        // Save to DynamoDB
        dynamoDBMapper.save(assessment);
        return assessment;
    }

    public Assessment getAssessment(String id) {
        Assessment assessment = dynamoDBMapper.load(Assessment.class, id);
        if (assessment == null) {
            throw new RuntimeException("Assessment not found");
        }
        return assessment;
    }

    public List<Assessment> getAssessmentsByTherapist(String therapistEmail) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":createdBy", new AttributeValue().withS(therapistEmail));

        DynamoDBQueryExpression<Assessment> queryExpression = new DynamoDBQueryExpression<Assessment>()
            .withKeyConditionExpression("createdBy = :createdBy")
            .withExpressionAttributeValues(eav);

        return dynamoDBMapper.query(Assessment.class, queryExpression);
    }

    public List<Assessment> getActiveAssessments() {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":isActive", new AttributeValue().withBOOL(true));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
            .withFilterExpression("isActive = :isActive")
            .withExpressionAttributeValues(eav);

        return dynamoDBMapper.scan(Assessment.class, scanExpression);
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
        dynamoDBMapper.save(existingAssessment);
        return existingAssessment;
    }

    public void deactivateAssessment(String id, User therapist) {
        Assessment assessment = getAssessment(id);

        // Verify ownership
        if (!assessment.getCreatedBy().equals(therapist.getEmail())) {
            throw new RuntimeException("Not authorized to deactivate this assessment");
        }

        assessment.setActive(false);
        dynamoDBMapper.save(assessment);
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

            // Validate multiple choice questions have options
            if (question.getType() == Assessment.QuestionType.MULTIPLE_CHOICE 
                && (question.getOptions() == null || question.getOptions().isEmpty())) {
                throw new RuntimeException("Multiple choice questions must have options");
            }

            // Validate scale questions have valid ranges
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