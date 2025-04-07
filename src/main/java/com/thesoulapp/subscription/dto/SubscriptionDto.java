package com.thesoulapp.subscription.dto;

import com.thesoulapp.subscription.model.SubscriptionTier;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SubscriptionDto {
    private SubscriptionTier tier;
    private LocalDateTime endDate; // Null if not applicable
    private boolean isActive;
} 