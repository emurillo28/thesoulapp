package com.thesoulapp.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class UserAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @OneToMany(mappedBy = "userAssessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    // Add potential field for score/result if calculated
    // private Integer score;
} 