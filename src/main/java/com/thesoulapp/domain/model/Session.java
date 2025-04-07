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
public class Session {
    private UUID id;
    private UUID therapistId;
    private UUID patientId;
    private SessionType type;
    private SessionStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private int durationMinutes;
    private String location;
    private String meetingUrl;
    private String notes;
    private String therapistNotes;
    private String patientNotes;
    private List<String> tags;
    private String paymentStatus;
    private double amount;
    private String currency;
    private String paymentMethod;
    private String invoiceId;
    private boolean isRecurring;
    private String recurrenceRule;
    private LocalDateTime nextOccurrence;
    private String cancellationReason;
    private LocalDateTime cancelledAt;
    private String reschedulingReason;
    private LocalDateTime rescheduledAt;
    private int rating;
    private String feedback;
    private String recordingUrl;
    private String transcriptUrl;
    private List<String> resources;
    private String customData;
    
    // Domain methods
    public boolean isUpcoming() {
        return status == SessionStatus.SCHEDULED && 
               scheduledAt.isAfter(LocalDateTime.now());
    }
    
    public boolean isInProgress() {
        return status == SessionStatus.IN_PROGRESS;
    }
    
    public boolean isCompleted() {
        return status == SessionStatus.COMPLETED;
    }
    
    public boolean isCancelled() {
        return status == SessionStatus.CANCELLED;
    }
    
    public boolean isRescheduled() {
        return status == SessionStatus.RESCHEDULED;
    }
    
    public void start() {
        this.status = SessionStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }
    
    public void end() {
        this.status = SessionStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
    }
    
    public void cancel(String reason) {
        this.status = SessionStatus.CANCELLED;
        this.cancellationReason = reason;
        this.cancelledAt = LocalDateTime.now();
    }
    
    public void reschedule(LocalDateTime newTime, String reason) {
        this.status = SessionStatus.RESCHEDULED;
        this.reschedulingReason = reason;
        this.rescheduledAt = LocalDateTime.now();
        this.scheduledAt = newTime;
    }
    
    public void addNote(String note, boolean isTherapistNote) {
        if (isTherapistNote) {
            this.therapistNotes = note;
        } else {
            this.patientNotes = note;
        }
    }
    
    public void addResource(String resourceUrl) {
        if (!resources.contains(resourceUrl)) {
            resources.add(resourceUrl);
        }
    }
    
    public void addFeedback(int rating, String feedback) {
        this.rating = rating;
        this.feedback = feedback;
    }
    
    public boolean isVirtual() {
        return meetingUrl != null && !meetingUrl.isEmpty();
    }
    
    public boolean isInPerson() {
        return location != null && !location.isEmpty();
    }
    
    public void calculateNextOccurrence() {
        if (isRecurring && recurrenceRule != null) {
            // TODO: Implement recurrence calculation based on recurrenceRule
        }
    }
} 