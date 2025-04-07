package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Payment {
    private UUID id;
    private UUID userId;
    private UUID sessionId;
    private PaymentType type;
    private PaymentStatus status;
    private double amount;
    private String currency;
    private String description;
    private String paymentMethod;
    private String paymentMethodId;
    private String transactionId;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
    private double refundAmount;
    private String refundReason;
    private String invoiceId;
    private String receiptUrl;
    private String failureReason;
    private String failureCode;
    private String metadata;
    private String customData;
    private boolean isRecurring;
    private String subscriptionId;
    private LocalDateTime nextBillingDate;
    private String couponCode;
    private double discountAmount;
    private double taxAmount;
    private double processingFee;
    private String billingAddress;
    private String shippingAddress;
    private String customerEmail;
    private String customerPhone;
    private String customerName;
    
    // Domain methods
    public boolean isSuccessful() {
        return status == PaymentStatus.SUCCEEDED;
    }
    
    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }
    
    public boolean isRefunded() {
        return status == PaymentStatus.REFUNDED;
    }
    
    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }
    
    public double getTotalAmount() {
        return amount + taxAmount + processingFee - discountAmount;
    }
    
    public void markAsPaid(String transactionId) {
        this.status = PaymentStatus.SUCCEEDED;
        this.transactionId = transactionId;
        this.paidAt = LocalDateTime.now();
    }
    
    public void markAsFailed(String reason, String code) {
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.failureCode = code;
    }
    
    public void refund(double refundAmount, String reason) {
        this.status = PaymentStatus.REFUNDED;
        this.refundAmount = refundAmount;
        this.refundReason = reason;
        this.refundedAt = LocalDateTime.now();
    }
    
    public void applyCoupon(String code, double discount) {
        this.couponCode = code;
        this.discountAmount = discount;
    }
    
    public void setRecurring(String subscriptionId, LocalDateTime nextBillingDate) {
        this.isRecurring = true;
        this.subscriptionId = subscriptionId;
        this.nextBillingDate = nextBillingDate;
    }
    
    public boolean isSubscription() {
        return isRecurring && subscriptionId != null;
    }
    
    public void updateNextBillingDate(LocalDateTime newDate) {
        if (isRecurring) {
            this.nextBillingDate = newDate;
        }
    }
    
    public boolean canBeRefunded() {
        return status == PaymentStatus.SUCCEEDED && 
               refundAmount == 0;
    }
    
    public double getRefundableAmount() {
        return getTotalAmount() - refundAmount;
    }
} 