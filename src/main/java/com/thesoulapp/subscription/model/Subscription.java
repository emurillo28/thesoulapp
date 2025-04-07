package com.thesoulapp.subscription.model;

import com.thesoulapp.domain.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionTier tier = SubscriptionTier.BASIC; // Default tier

    private LocalDateTime startDate;
    private LocalDateTime endDate; // Null for non-expiring/basic
    private LocalDateTime lastPaymentDate;
    private String paymentProviderId; // e.g., Stripe customer ID

    @Column(nullable = false)
    private boolean active = true;
} 