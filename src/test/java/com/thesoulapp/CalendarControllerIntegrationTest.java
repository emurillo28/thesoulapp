package com.thesoulapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesoulapp.domain.user.User;
import com.thesoulapp.domain.calendar.CalendarEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.ZonedDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CalendarControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String getPatientJwt() throws Exception {
        var signup = new SignupRequest("patient2@example.com", "password", User.Role.PATIENT, "{}");
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk());
        var login = new LoginRequest("patient2@example.com", "password");
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(token).get("token").asText();
    }

    @Test
    void testCreateUpdateDeleteEvent() throws Exception {
        String jwt = getPatientJwt();
        var now = ZonedDateTime.now();
        var create = new CalendarEventRequest(CalendarEvent.EventType.PERSONAL_ENTRY, now, now.plusHours(1), "details");
        // Create
        String eventJson = mockMvc.perform(post("/api/calendar")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String id = objectMapper.readTree(eventJson).get("id").asText();
        // Update
        var update = new CalendarEventRequest(CalendarEvent.EventType.PERSONAL_ENTRY, now, now.plusHours(2), "updated");
        mockMvc.perform(put("/api/calendar/" + id)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.details").value("updated"));
        // Get
        mockMvc.perform(get("/api/calendar")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));
        // Delete
        mockMvc.perform(delete("/api/calendar/" + id)
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    record SignupRequest(String email, String password, User.Role role, String profile) {}
    record LoginRequest(String email, String password) {}
    record CalendarEventRequest(CalendarEvent.EventType type, ZonedDateTime startTime, ZonedDateTime endTime, String details) {}
} 