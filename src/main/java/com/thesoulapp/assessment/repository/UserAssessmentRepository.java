package com.thesoulapp.assessment.repository;

import com.thesoulapp.domain.model.UserAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAssessmentRepository extends JpaRepository<UserAssessment, Long> {
} 