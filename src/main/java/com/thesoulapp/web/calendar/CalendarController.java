package com.thesoulapp.web.calendar;

import com.thesoulapp.application.calendar.CalendarService;
import com.thesoulapp.domain.calendar.CalendarEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendar")
@Tag(name = "Calendar Event Management", description = "Endpoints for managing calendar events")
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Operation(summary = "Create a calendar event (owner only)")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CalendarEventRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(getUserId(authentication));
        CalendarEvent event = CalendarEvent.builder()
                .userId(userId)
                .type(request.type())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .details(request.details())
                .build();
        return ResponseEntity.ok(CalendarEventResponse.from(calendarService.createEvent(event)));
    }

    @Operation(summary = "Update a calendar event (owner only)")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody CalendarEventRequest request, Authentication authentication) {
        UUID userId = UUID.fromString(getUserId(authentication));
        CalendarEvent event = CalendarEvent.builder()
                .userId(userId)
                .type(request.type())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .details(request.details())
                .build();
        return ResponseEntity.ok(CalendarEventResponse.from(calendarService.updateEvent(id, event, userId)));
    }

    @Operation(summary = "Delete a calendar event (owner only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, Authentication authentication) {
        UUID userId = UUID.fromString(getUserId(authentication));
        calendarService.deleteEvent(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get events for user, filter by type and date")
    @GetMapping
    public ResponseEntity<List<CalendarEventResponse>> getEvents(@RequestParam(required = false) CalendarEvent.EventType type,
                                                                @RequestParam(required = false) ZonedDateTime from,
                                                                @RequestParam(required = false) ZonedDateTime to,
                                                                Authentication authentication) {
        UUID userId = UUID.fromString(getUserId(authentication));
        return ResponseEntity.ok(calendarService.getEvents(userId, type, from, to).stream().map(CalendarEventResponse::from).toList());
    }

    @Operation(summary = "Therapist: set availability")
    @PostMapping("/availability")
    public ResponseEntity<?> setAvailability(@RequestBody AvailabilityRequest request, Authentication authentication) {
        if (!hasRole(authentication, "THERAPIST")) {
            return ResponseEntity.status(403).body("Only therapists can set availability");
        }
        UUID therapistId = UUID.fromString(getUserId(authentication));
        CalendarEvent event = calendarService.setAvailability(therapistId, request.startTime(), request.endTime(), request.details());
        return ResponseEntity.ok(CalendarEventResponse.from(event));
    }

    @Operation(summary = "Patient: book appointment with therapist")
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody BookAppointmentRequest request, Authentication authentication) {
        if (!hasRole(authentication, "PATIENT")) {
            return ResponseEntity.status(403).body("Only patients can book appointments");
        }
        UUID patientId = UUID.fromString(getUserId(authentication));
        CalendarEvent event = calendarService.bookAppointment(patientId, request.therapistId(), request.startTime(), request.endTime(), request.details());
        return ResponseEntity.ok(CalendarEventResponse.from(event));
    }

    private boolean hasRole(Authentication authentication, String role) {
        return authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    private String getUserId(Authentication authentication) {
        return authentication.getDetails().toString();
    }

    @Builder
    public record CalendarEventRequest(CalendarEvent.EventType type, ZonedDateTime startTime, ZonedDateTime endTime, String details) {}
    @Builder
    public record AvailabilityRequest(ZonedDateTime startTime, ZonedDateTime endTime, String details) {}
    @Builder
    public record BookAppointmentRequest(UUID therapistId, ZonedDateTime startTime, ZonedDateTime endTime, String details) {}
    @Builder
    public record CalendarEventResponse(String id, String userId, String type, ZonedDateTime startTime, ZonedDateTime endTime, String details, String createdAt) {
        public static CalendarEventResponse from(CalendarEvent e) {
            return CalendarEventResponse.builder()
                    .id(e.getId().toString())
                    .userId(e.getUserId().toString())
                    .type(e.getType().name())
                    .startTime(e.getStartTime())
                    .endTime(e.getEndTime())
                    .details(e.getDetails())
                    .createdAt(e.getCreatedAt().toString())
                    .build();
        }
    }
} 