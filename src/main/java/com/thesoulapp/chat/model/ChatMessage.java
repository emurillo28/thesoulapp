package com.thesoulapp.chat.model;

import com.thesoulapp.domain.model.BaseEntity;
import com.thesoulapp.domain.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a chat message between two users in the system.
 * Extends BaseEntity to inherit common fields like id, createdAt, and updatedAt.
 * The sentAt timestamp is mapped to the inherited createdAt field.
 */
@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class ChatMessage extends BaseEntity {

    // ID field is inherited from BaseEntity

    /**
     * The user who sent the message
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @NotNull(message = "Sender cannot be null")
    private User sender;

    /**
     * The user who received the message
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @NotNull(message = "Receiver cannot be null")
    private User receiver;

    /**
     * The text content of the message
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Message content cannot be blank")
    private String content;

    // Note: sentAt timestamp is represented by the createdAt field inherited from BaseEntity
    
    /**
     * Timestamp when the message was read by the receiver
     */
    @Column
    private LocalDateTime readAt;

    /**
     * Checks if the message has been read
     * @return true if message has been read, false otherwise
     */
    public boolean isRead() {
        return readAt != null;
    }

    /**
     * Marks the message as read if it hasn't been read already
     */
    public void markAsRead() {
        if (this.readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }

    // Package-private setters for JPA relationship management
    void setSender(User sender) {
        this.sender = sender;
    }
    
    void setReceiver(User receiver) {
        this.receiver = receiver;
    }
} 