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
public class Therapist {
    private UUID id;
    private UUID userId;
    private String licenseNumber;
    private String specialization;
    private List<String> therapyTypes;
    private String bio;
    private int yearsOfExperience;
    private List<String> languages;
    private List<String> certifications;
    private boolean isAvailable;
    private int maxPatients;
    private int currentPatients;
    private double hourlyRate;
    private String education;
    private LocalDateTime availabilityUpdatedAt;
    private List<String> acceptedInsurance;
    private boolean isVerified;
    private LocalDateTime verificationDate;
    private String verificationDocumentUrl;
    
    // Domain methods
    public boolean canAcceptNewPatients() {
        return isAvailable && currentPatients < maxPatients;
    }
    
    public void updateAvailability(boolean available) {
        this.isAvailable = available;
        this.availabilityUpdatedAt = LocalDateTime.now();
    }
    
    public void incrementPatientCount() {
        if (canAcceptNewPatients()) {
            this.currentPatients++;
        }
    }
    
    public void decrementPatientCount() {
        if (this.currentPatients > 0) {
            this.currentPatients--;
        }
    }
    
    public void verify(String documentUrl) {
        this.isVerified = true;
        this.verificationDate = LocalDateTime.now();
        this.verificationDocumentUrl = documentUrl;
    }
    
    public boolean isFullyQualified() {
        return isVerified && 
               !licenseNumber.isEmpty() && 
               !specialization.isEmpty() && 
               !therapyTypes.isEmpty();
    }
} 