package com.thesoulapp.domain.model;

public enum PaymentType {
    SESSION("Therapy session payment"),
    SUBSCRIPTION("Recurring subscription"),
    PACKAGE("Therapy package"),
    WORKSHOP("Workshop registration"),
    RESOURCE("Resource purchase"),
    DONATION("Donation"),
    REFUND("Payment refund"),
    ADJUSTMENT("Payment adjustment");
    
    private final String description;
    
    PaymentType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isRecurring() {
        return this == SUBSCRIPTION;
    }
    
    public boolean isOneTime() {
        return this == SESSION || 
               this == PACKAGE || 
               this == WORKSHOP || 
               this == RESOURCE || 
               this == DONATION;
    }
    
    public boolean isRefund() {
        return this == REFUND;
    }
    
    public boolean isAdjustment() {
        return this == ADJUSTMENT;
    }
    
    public boolean requiresSession() {
        return this == SESSION;
    }
    
    public boolean requiresPackage() {
        return this == PACKAGE;
    }
    
    public boolean requiresWorkshop() {
        return this == WORKSHOP;
    }
    
    public boolean requiresResource() {
        return this == RESOURCE;
    }
    
    public boolean isDonation() {
        return this == DONATION;
    }
} 