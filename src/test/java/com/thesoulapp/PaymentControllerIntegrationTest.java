package com.thesoulapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesoulapp.domain.user.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String getPatientJwt() throws Exception {
        var signup = new SignupRequest("patient4@example.com", "password", User.Role.PATIENT, "{}");
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk());
        var login = new LoginRequest("patient4@example.com", "password");
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(token).get("token").asText();
    }

    @Test
    @Disabled("Stripe integration test requires test keys and setup")
    void testChargePatient() throws Exception {
        String jwt = getPatientJwt();
        var payment = new PaymentRequest(UUID.randomUUID(), new BigDecimal("10.00"), "pm_card_visa");
        mockMvc.perform(post("/api/payments")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isOk());
    }

    record SignupRequest(String email, String password, User.Role role, String profile) {}
    record LoginRequest(String email, String password) {}
    record PaymentRequest(UUID therapistId, BigDecimal amount, String paymentMethodId) {}
} 