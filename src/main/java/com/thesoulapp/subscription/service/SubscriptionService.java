package com.thesoulapp.subscription.service;

import com.thesoulapp.subscription.dto.SubscriptionDto;
import com.thesoulapp.subscription.model.Subscription;
import com.thesoulapp.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public SubscriptionDto getUserSubscription(String userEmail) {
        Subscription subscription = subscriptionRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Subscription not found for user: " + userEmail)); 
                // Or return default BASIC tier DTO

        return SubscriptionDto.builder()
                .tier(subscription.getTier())
                .endDate(subscription.getEndDate())
                .isActive(subscription.isActive() && (subscription.getEndDate() == null || subscription.getEndDate().isAfter(java.time.LocalDateTime.now())))
                .build();
    }
    
    @Transactional(readOnly = true)
    public Subscription getSubscriptionEntityByUserEmail(String userEmail) {
         return subscriptionRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Subscription not found for user: " + userEmail));
    }
    
    // Add methods for creating/updating subscriptions (e.g., via webhooks from payment provider)
    // public void updateSubscription(...) { ... }
    // public void createInitialSubscription(User user) { ... } -> Call this on user signup
} 