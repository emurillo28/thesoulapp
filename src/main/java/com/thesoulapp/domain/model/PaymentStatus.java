package com.thesoulapp.domain.model;

public enum PaymentStatus {
    PENDING("Payment is pending"),
    PROCESSING("Payment is being processed"),
    SUCCEEDED("Payment was successful"),
    FAILED("Payment failed"),
    REFUNDED("Payment was refunded"),
    PARTIALLY_REFUNDED("Payment was partially refunded"),
    CANCELLED("Payment was cancelled"),
    EXPIRED("Payment link expired"),
    REQUIRES_ACTION("Payment requires additional action"),
    REQUIRES_CONFIRMATION("Payment requires confirmation");
    
    private final String description;
    
    PaymentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isTerminal() {
        return this == SUCCEEDED || 
               this == FAILED || 
               this == REFUNDED || 
               this == CANCELLED || 
               this == EXPIRED;
    }
    
    public boolean isActive() {
        return this == PENDING || 
               this == PROCESSING || 
               this == REQUIRES_ACTION || 
               this == REQUIRES_CONFIRMATION;
    }
    
    public boolean isSuccessful() {
        return this == SUCCEEDED;
    }
    
    public boolean isFailed() {
        return this == FAILED || 
               this == EXPIRED;
    }
    
    public boolean isRefunded() {
        return this == REFUNDED || 
               this == PARTIALLY_REFUNDED;
    }
    
    public boolean requiresAction() {
        return this == REQUIRES_ACTION || 
               this == REQUIRES_CONFIRMATION;
    }
    
    public boolean canBeRefunded() {
        return this == SUCCEEDED;
    }
    
    public boolean canBeCancelled() {
        return this == PENDING || 
               this == PROCESSING;
    }
} 