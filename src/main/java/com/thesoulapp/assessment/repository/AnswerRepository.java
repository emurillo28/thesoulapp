package com.thesoulapp.assessment.repository;

import com.thesoulapp.domain.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
} 