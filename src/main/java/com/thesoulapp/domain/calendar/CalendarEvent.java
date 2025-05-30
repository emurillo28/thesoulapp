package com.thesoulapp.domain.calendar;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "calendar_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(nullable = false)
    private ZonedDateTime startTime;

    @Column(nullable = false)
    private ZonedDateTime endTime;

    @Column(columnDefinition = "TEXT")
    private String details; // JSONB as String for now

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public enum EventType {
        APPOINTMENT, PERSONAL_ENTRY, EMOTION_TRACKING, AVAILABILITY
    }
} 