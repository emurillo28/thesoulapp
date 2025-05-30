package com.thesoulapp.domain.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, UUID> {
    List<CalendarEvent> findByUserId(UUID userId);
    List<CalendarEvent> findByUserIdAndType(UUID userId, CalendarEvent.EventType type);
    List<CalendarEvent> findByUserIdAndTypeAndStartTimeBetween(UUID userId, CalendarEvent.EventType type, ZonedDateTime start, ZonedDateTime end);
} 