package com.thesoulapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesoulapp.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AssessmentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    // Helper: get JWT for a therapist
    private String getTherapistJwt() throws Exception {
        var signup = new SignupRequest("therapist@example.com", "password", User.Role.THERAPIST, "{}");
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk());
        var login = new LoginRequest("therapist@example.com", "password");
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(token).get("token").asText();
    }

    @Test
    void testCreateListAndApproveAssessment() throws Exception {
        String jwt = getTherapistJwt();
        // Create
        var create = new CreateAssessmentRequest("Test Assessment", "Desc", "[{\"q\":\"How are you?\"}]");
        String assessmentJson = mockMvc.perform(post("/api/assessments")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(create)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String id = objectMapper.readTree(assessmentJson).get("id").asText();
        // List
        mockMvc.perform(get("/api/assessments")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Assessment"));
        // Approve
        mockMvc.perform(post("/api/assessments/" + id + "/approve")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approvedBy[0]").exists());
    }

    record SignupRequest(String email, String password, User.Role role, String profile) {}
    record LoginRequest(String email, String password) {}
    record CreateAssessmentRequest(String title, String description, String questions) {}
} 