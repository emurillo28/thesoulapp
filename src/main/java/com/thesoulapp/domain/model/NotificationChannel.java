package com.thesoulapp.domain.model;

public enum NotificationChannel {
    EMAIL("Email notification"),
    SMS("Text message notification"),
    PUSH("Mobile push notification"),
    IN_APP("In-app notification"),
    WEB_PUSH("Web push notification"),
    VOICE("Voice call notification"),
    WHATSAPP("WhatsApp message");
    
    private final String description;
    
    NotificationChannel(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isMobile() {
        return this == SMS || 
               this == PUSH || 
               this == WHATSAPP;
    }
    
    public boolean isWeb() {
        return this == EMAIL || 
               this == WEB_PUSH || 
               this == IN_APP;
    }
    
    public boolean isVoice() {
        return this == VOICE;
    }
    
    public boolean requiresExternalService() {
        return this == EMAIL || 
               this == SMS || 
               this == WHATSAPP || 
               this == VOICE;
    }
    
    public int getDefaultRetryCount() {
        return switch (this) {
            case VOICE -> 3;
            case SMS, WHATSAPP -> 2;
            case EMAIL, PUSH, WEB_PUSH, IN_APP -> 1;
        };
    }
} 