package com.thesoulapp.auth.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.thesoulapp.auth.dto.AuthResponse;
import com.thesoulapp.auth.dto.LoginRequest;
import com.thesoulapp.auth.dto.SignUpRequest;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.model.UserRole;
import com.thesoulapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private AWSCognitoIdentityProvider cognitoClient;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    private SignUpRequest signUpRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@test.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setFullName("Test User");
        signUpRequest.setRole(UserRole.PATIENT);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setFullName("Test User");
        user.setRole(UserRole.PATIENT);
        user.setPassword("encodedPassword");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("jwt-token");
    }

    @Test
    void signUp_ShouldCreateUserInCognitoAndDatabase() {
        // Arrange
        SignUpResult signUpResult = new SignUpResult();
        when(cognitoClient.signUp(any(SignUpRequest.class))).thenReturn(signUpResult);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        AuthResponse response = authService.signUp(signUpRequest);

        // Assert
        verify(cognitoClient).signUp(any(SignUpRequest.class));
        verify(cognitoClient).adminConfirmSignUp(any(AdminConfirmSignUpRequest.class));
        verify(userRepository).save(any(User.class));
        
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(user, response.getUser());
    }

    @Test
    void login_ShouldAuthenticateAndReturnToken() {
        // Arrange
        AdminInitiateAuthResult authResult = new AdminInitiateAuthResult();
        when(cognitoClient.adminInitiateAuth(any(AdminInitiateAuthRequest.class))).thenReturn(authResult);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        verify(cognitoClient).adminInitiateAuth(any(AdminInitiateAuthRequest.class));
        verify(userRepository).findByEmail(loginRequest.getEmail());
        
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(user, response.getUser());
    }

    @Test
    void resetPassword_ShouldInitiateReset() {
        // Act
        authService.resetPassword("test@test.com");

        // Assert
        ArgumentCaptor<AdminResetUserPasswordRequest> captor = 
            ArgumentCaptor.forClass(AdminResetUserPasswordRequest.class);
        verify(cognitoClient).adminResetUserPassword(captor.capture());
        
        AdminResetUserPasswordRequest request = captor.getValue();
        assertEquals("test@test.com", request.getUsername());
    }
} 