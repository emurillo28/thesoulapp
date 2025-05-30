package com.thesoulapp.domain.assessment;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private UUID createdBy; // therapist ID

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "assessment_approved_by", joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "therapist_id")
    private List<UUID> approvedBy;

    @Column(columnDefinition = "TEXT")
    private String questions; // JSONB as String for now

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
} 