package com.thesoulapp.subscription.model;

import com.thesoulapp.domain.model.BaseEntity;
import com.thesoulapp.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Subscription extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @MapsId
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private SubscriptionTier tier = SubscriptionTier.BASIC;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime lastPaymentDate;
    private String paymentProviderId;

    @Column(nullable = false)
    private boolean active = true;

    public boolean isActiveNow() {
        return this.active && (this.endDate == null || this.endDate.isAfter(LocalDateTime.now()));
    }

    public void changeTier(SubscriptionTier newTier, LocalDateTime newEndDate) {
        this.tier = newTier;
        this.endDate = newEndDate;
        this.active = true;
        this.lastPaymentDate = LocalDateTime.now();
    }

    public void deactivate() {
        this.active = false;
    }

    void setUser(User user) {
        this.user = user;
    }
} 