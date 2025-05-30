package com.thesoulapp.application.calendar;

import com.thesoulapp.domain.calendar.CalendarEvent;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface CalendarService {
    CalendarEvent createEvent(CalendarEvent event);
    CalendarEvent updateEvent(UUID eventId, CalendarEvent event, UUID userId);
    void deleteEvent(UUID eventId, UUID userId);
    List<CalendarEvent> getEvents(UUID userId, CalendarEvent.EventType type, ZonedDateTime from, ZonedDateTime to);
    CalendarEvent setAvailability(UUID therapistId, ZonedDateTime start, ZonedDateTime end, String details);
    CalendarEvent bookAppointment(UUID patientId, UUID therapistId, ZonedDateTime start, ZonedDateTime end, String details);
} 