package com.thesoulapp.assessment.controller;

import com.thesoulapp.assessment.dto.AssessmentStructureDto;
import com.thesoulapp.assessment.dto.SubmitAssessmentDto;
import com.thesoulapp.assessment.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;

    // Endpoint for therapist to create/update assessments (Separate concern, potentially admin API)
    // @PostMapping("/create") ...

    // Endpoint for user to list available assessments
    @GetMapping
    public ResponseEntity<List<AssessmentStructureDto>> listAvailableAssessments(Authentication authentication) {
        // Could filter based on user role, subscription, etc. if needed
        return ResponseEntity.ok(assessmentService.getAvailableAssessments());
    }

    // Endpoint for user to get a specific assessment structure
    @GetMapping("/{assessmentId}/structure")
    public ResponseEntity<AssessmentStructureDto> getAssessmentStructure(
            @PathVariable Long assessmentId, Authentication authentication) {
        return ResponseEntity.ok(assessmentService.getAssessmentStructureById(assessmentId));
    }

    // Endpoint for user to submit their answers
    @PostMapping("/{assessmentId}/submit")
    public ResponseEntity<Void> submitAssessment(
            @PathVariable Long assessmentId,
            @Valid @RequestBody SubmitAssessmentDto submissionDto,
            Authentication authentication) {
        assessmentService.submitUserAssessment(assessmentId, submissionDto, authentication.getName());
        return ResponseEntity.ok().build();
    }
    
     // Endpoint for user to get their assessment history/results (Add later if needed)
    // @GetMapping("/history") ...
} 