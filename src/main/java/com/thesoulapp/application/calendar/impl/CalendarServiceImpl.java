package com.thesoulapp.application.calendar.impl;

import com.thesoulapp.application.calendar.CalendarService;
import com.thesoulapp.domain.calendar.CalendarEvent;
import com.thesoulapp.domain.calendar.CalendarEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CalendarServiceImpl implements CalendarService {
    private final CalendarEventRepository calendarEventRepository;

    @Autowired
    public CalendarServiceImpl(CalendarEventRepository calendarEventRepository) {
        this.calendarEventRepository = calendarEventRepository;
    }

    @Override
    public CalendarEvent createEvent(CalendarEvent event) {
        return calendarEventRepository.save(event);
    }

    @Override
    public CalendarEvent updateEvent(UUID eventId, CalendarEvent event, UUID userId) {
        CalendarEvent existing = calendarEventRepository.findById(eventId).orElseThrow();
        if (!existing.getUserId().equals(userId)) throw new SecurityException("Not owner");
        event.setId(eventId);
        event.setUserId(userId);
        return calendarEventRepository.save(event);
    }

    @Override
    public void deleteEvent(UUID eventId, UUID userId) {
        CalendarEvent existing = calendarEventRepository.findById(eventId).orElseThrow();
        if (!existing.getUserId().equals(userId)) throw new SecurityException("Not owner");
        calendarEventRepository.deleteById(eventId);
    }

    @Override
    public List<CalendarEvent> getEvents(UUID userId, CalendarEvent.EventType type, ZonedDateTime from, ZonedDateTime to) {
        if (type != null && from != null && to != null) {
            return calendarEventRepository.findByUserIdAndTypeAndStartTimeBetween(userId, type, from, to);
        } else if (type != null) {
            return calendarEventRepository.findByUserIdAndType(userId, type);
        } else {
            return calendarEventRepository.findByUserId(userId);
        }
    }

    @Override
    public CalendarEvent setAvailability(UUID therapistId, ZonedDateTime start, ZonedDateTime end, String details) {
        CalendarEvent event = CalendarEvent.builder()
                .userId(therapistId)
                .type(CalendarEvent.EventType.AVAILABILITY)
                .startTime(start)
                .endTime(end)
                .details(details)
                .build();
        return calendarEventRepository.save(event);
    }

    @Override
    public CalendarEvent bookAppointment(UUID patientId, UUID therapistId, ZonedDateTime start, ZonedDateTime end, String details) {
        // Check therapist availability
        List<CalendarEvent> availabilities = calendarEventRepository.findByUserIdAndTypeAndStartTimeBetween(
                therapistId, CalendarEvent.EventType.AVAILABILITY, start, end);
        if (availabilities.isEmpty()) throw new IllegalStateException("Therapist not available");
        CalendarEvent event = CalendarEvent.builder()
                .userId(patientId)
                .type(CalendarEvent.EventType.APPOINTMENT)
                .startTime(start)
                .endTime(end)
                .details(details)
                .build();
        return calendarEventRepository.save(event);
    }
} 