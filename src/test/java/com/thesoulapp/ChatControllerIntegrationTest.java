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
public class ChatControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String getPatientJwt() throws Exception {
        var signup = new SignupRequest("patient3@example.com", "password", User.Role.PATIENT, "{}");
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk());
        var login = new LoginRequest("patient3@example.com", "password");
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(token).get("token").asText();
    }

    @Test
    void testFetchMessagesHistory() throws Exception {
        String jwt = getPatientJwt();
        String roomId = UUID.randomUUID().toString();
        // No messages yet
        mockMvc.perform(get("/api/chat/" + roomId + "/messages")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    record SignupRequest(String email, String password, User.Role role, String profile) {}
    record LoginRequest(String email, String password) {}
} 