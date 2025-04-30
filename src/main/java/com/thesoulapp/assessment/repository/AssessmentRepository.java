package com.thesoulapp.assessment.repository;

import com.thesoulapp.assessment.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, String> {
    
    List<Assessment> findByCreatedBy(String therapistEmail);
    
    List<Assessment> findByIsActiveTrue();
} 