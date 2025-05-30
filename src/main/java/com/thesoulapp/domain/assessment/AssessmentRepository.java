package com.thesoulapp.domain.assessment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, UUID> {
} 