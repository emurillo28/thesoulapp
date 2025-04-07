package com.thesoulapp.subscription.controller;

import com.thesoulapp.subscription.dto.SubscriptionDto;
import com.thesoulapp.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/me")
    public ResponseEntity<SubscriptionDto> getCurrentUserSubscription(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(subscriptionService.getUserSubscription(email));
    }
    
    // Endpoints for managing subscriptions (upgrade/cancel) would go here
    // These often involve integration with payment providers (Stripe, etc.)
    // @PostMapping("/upgrade") ...
    // @PostMapping("/cancel") ...
} 