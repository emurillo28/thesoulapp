package com.thesoulapp.assessment.repository;

import com.thesoulapp.domain.model.Survey;
import com.thesoulapp.domain.model.SurveyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    List<Survey> findByStatus(SurveyStatus status);
    Optional<Survey> findByIdAndStatus(Long id, SurveyStatus status);
} 