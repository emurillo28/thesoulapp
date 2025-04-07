package com.thesoulapp.auth.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.thesoulapp.auth.dto.AuthResponse;
import com.thesoulapp.auth.dto.LoginRequest;
import com.thesoulapp.auth.dto.SignUpRequest;
import com.thesoulapp.auth.dto.VerificationRequest;
import com.thesoulapp.user.model.User;
import com.thesoulapp.user.repository.UserRepository;
import com.thesoulapp.subscription.model.Subscription;
import com.thesoulapp.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;
import com.thesoulapp.auth.service.CognitoService;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AWSCognitoIdentityProvider cognitoClient;
    private final AmazonSNS snsClient;
    private final AmazonSimpleEmailService sesClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SubscriptionRepository subscriptionRepository;
    private final CognitoService cognitoService;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    @Value("${aws.sns.verificationTopic}")
    private String verificationTopic;

    @Value("${aws.ses.sourceEmail}")
    private String sourceEmail;

    @Transactional
    public AuthResponse signUp(SignUpRequest request) {
        try {
            // Register with Cognito first
            String cognitoUserId = cognitoService.signUp(
                request.getEmail(), 
                request.getPassword(),
                request.getName()
            );
            
            // Create local user
            User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setRole(UserRole.USER);
            user.setExternalId(cognitoUserId); // Store Cognito user ID
            
            User savedUser = userRepository.save(user);
            
            // Create subscription
            Subscription subscription = new Subscription();
            subscription.setUser(savedUser);
            subscription.setStartDate(LocalDateTime.now());
            subscription.setActive(true);
            subscriptionRepository.save(subscription);
            
            // No token returned until user confirms email
            return new AuthResponse(null, savedUser);
            
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public AuthResponse confirmSignUp(String email, String confirmationCode) {
        try {
            // Confirm with Cognito
            cognitoService.confirmSignUp(email, confirmationCode);
            
            // Get local user
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
            
            // Generate JWT token
            String jwtToken = jwtService.generateToken(user);
            
            return new AuthResponse(jwtToken, user);
        } catch (Exception e) {
            throw new RuntimeException("Confirmation failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            // Authenticate with Cognito
            AdminInitiateAuthResult authResult = cognitoService.authenticate(request.getEmail(), request.getPassword());
            AuthenticationResultType resultType = authResult.getAuthenticationResult();
            
            // Get or create local user
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseGet(() -> {
                        // Create a new user if they don't exist locally yet
                        User newUser = new User();
                        newUser.setEmail(request.getEmail());
                        newUser.setName(request.getEmail().split("@")[0]); // Default name from email
                        newUser.setRole(UserRole.USER);
                        return userRepository.save(newUser);
                    });
            
            // Generate our JWT token
            String jwtToken = jwtService.generateToken(user);
            
            return new AuthResponse(jwtToken, user);
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void resetPassword(String email) {
        try {
            // Check if user exists locally
            userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
            
            // Initiate forgot password flow in Cognito
            cognitoService.forgotPassword(email);
        } catch (Exception e) {
            throw new RuntimeException("Password reset failed: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void confirmResetPassword(String email, String confirmationCode, String newPassword) {
        try {
            // Confirm password reset in Cognito
            cognitoService.confirmForgotPassword(email, confirmationCode, newPassword);
        } catch (Exception e) {
            throw new RuntimeException("Password reset confirmation failed: " + e.getMessage(), e);
        }
    }

    private void sendVerificationEmail(String email) {
        SendEmailRequest request = new SendEmailRequest()
            .withDestination(new Destination().withToAddresses(email))
            .withMessage(new Message()
                .withBody(new Body()
                    .withHtml(new Content()
                        .withCharset("UTF-8")
                        .withData("Please verify your email by clicking the link below:")))
                .withSubject(new Content()
                    .withCharset("UTF-8")
                    .withData("Verify your TheSoulApp account")))
            .withSource(sourceEmail);

        sesClient.sendEmail(request);
    }

    private void sendVerificationSMS(String phoneNumber) {
        PublishRequest request = new PublishRequest()
            .withMessage("Welcome to TheSoulApp! Your verification code is: ")
            .withPhoneNumber(phoneNumber);

        snsClient.publish(request);
    }

    private void sendPasswordResetEmail(String email, String resetCode) {
        SendEmailRequest request = new SendEmailRequest()
            .withDestination(new Destination().withToAddresses(email))
            .withMessage(new Message()
                .withBody(new Body()
                    .withHtml(new Content()
                        .withCharset("UTF-8")
                        .withData("Your password reset code is: " + resetCode)))
                .withSubject(new Content()
                    .withCharset("UTF-8")
                    .withData("Reset your TheSoulApp password")))
            .withSource(sourceEmail);

        sesClient.sendEmail(request);
    }

    private void sendPasswordResetSMS(String phoneNumber, String resetCode) {
        PublishRequest request = new PublishRequest()
            .withMessage("Your TheSoulApp password reset code is: " + resetCode)
            .withPhoneNumber(phoneNumber);

        snsClient.publish(request);
    }
} 