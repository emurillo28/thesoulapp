package com.thesoulapp.web.assessment;

import com.thesoulapp.application.assessment.AssessmentService;
import com.thesoulapp.domain.assessment.Assessment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assessments")
@Tag(name = "Assessment Management", description = "Endpoints for managing assessments")
public class AssessmentController {
    private final AssessmentService assessmentService;

    @Autowired
    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @Operation(summary = "Create a new assessment (therapist only)")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateAssessmentRequest request, Authentication authentication) {
        // Only therapists can create
        if (!hasRole(authentication, "THERAPIST")) {
            return ResponseEntity.status(403).body("Only therapists can create assessments");
        }
        UUID therapistId = UUID.fromString(getUserId(authentication));
        Assessment assessment = assessmentService.createAssessment(request.title(), request.description(), therapistId, request.questions());
        return ResponseEntity.ok(AssessmentResponse.from(assessment));
    }

    @Operation(summary = "List all assessments")
    @GetMapping
    public ResponseEntity<List<AssessmentResponse>> list() {
        return ResponseEntity.ok(assessmentService.listAssessments().stream().map(AssessmentResponse::from).toList());
    }

    @Operation(summary = "Approve an assessment (therapist only)")
    @PostMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable UUID id, Authentication authentication) {
        if (!hasRole(authentication, "THERAPIST")) {
            return ResponseEntity.status(403).body("Only therapists can approve assessments");
        }
        UUID therapistId = UUID.fromString(getUserId(authentication));
        Assessment assessment = assessmentService.approveAssessment(id, therapistId);
        return ResponseEntity.ok(AssessmentResponse.from(assessment));
    }

    private boolean hasRole(Authentication authentication, String role) {
        // JWT stores role as a claim, but in this setup, authorities are empty. You may want to enhance JwtAuthenticationFilter to set authorities.
        // For now, parse the JWT claims from the principal (email) is not enough, so this is a placeholder.
        // In production, you should extract the role from JWT claims.
        return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private String getUserId(Authentication authentication) {
        return authentication.getDetails().toString();
    }

    @Builder
    public record CreateAssessmentRequest(String title, String description, String questions) {}
    @Builder
    public record AssessmentResponse(String id, String title, String description, String createdBy, List<String> approvedBy, String questions, String createdAt) {
        public static AssessmentResponse from(Assessment a) {
            return AssessmentResponse.builder()
                    .id(a.getId().toString())
                    .title(a.getTitle())
                    .description(a.getDescription())
                    .createdBy(a.getCreatedBy().toString())
                    .approvedBy(a.getApprovedBy().stream().map(UUID::toString).toList())
                    .questions(a.getQuestions())
                    .createdAt(a.getCreatedAt().toString())
                    .build();
        }
    }
} 