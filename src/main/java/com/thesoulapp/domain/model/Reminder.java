package com.thesoulapp.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Reminder {
    private UUID id;
    private UUID userId;
    private String title;
    private String description;
    private ReminderType type;
    private ReminderStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private List<NotificationChannel> channels;
    private int retryCount;
    private int maxRetries;
    private String errorMessage;
    private String metadata;
    private boolean isRecurring;
    private String recurrenceRule;
    private LocalDateTime nextOccurrence;
    private LocalDateTime endDate;
    private int priority;
    private String category;
    private List<String> tags;
    private String actionUrl;
    private String customData;
    
    // Domain methods
    public boolean isOverdue() {
        return status == ReminderStatus.PENDING && 
               LocalDateTime.now().isAfter(scheduledAt);
    }
    
    public boolean canBeSent() {
        return status == ReminderStatus.PENDING && 
               !isOverdue() && 
               retryCount < maxRetries;
    }
    
    public void markAsSent() {
        this.status = ReminderStatus.SENT;
        this.sentAt = LocalDateTime.now();
        this.retryCount = 0;
    }
    
    public void markAsFailed(String error) {
        this.status = ReminderStatus.FAILED;
        this.errorMessage = error;
        this.retryCount++;
    }
    
    public void reschedule(LocalDateTime newTime) {
        this.scheduledAt = newTime;
        this.status = ReminderStatus.PENDING;
        this.retryCount = 0;
    }
    
    public boolean shouldReschedule() {
        return status == ReminderStatus.FAILED && 
               retryCount < maxRetries;
    }
    
    public void calculateNextOccurrence() {
        if (isRecurring && recurrenceRule != null) {
            // TODO: Implement recurrence calculation based on recurrenceRule
            // This could use a library like RRule for complex recurrence patterns
        }
    }
    
    public boolean isHighPriority() {
        return priority >= 8;
    }
    
    public boolean isMediumPriority() {
        return priority >= 5 && priority < 8;
    }
    
    public boolean isLowPriority() {
        return priority < 5;
    }
} 