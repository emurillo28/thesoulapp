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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AWSCognitoIdentityProvider cognitoClient;
    private final AmazonSNS snsClient;
    private final AmazonSimpleEmailService sesClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
        // First, create user in Cognito with additional attributes
        SignUpRequest cognitoRequest = new SignUpRequest()
                .withClientId(clientId)
                .withUsername(request.getEmail())
                .withPassword(request.getPassword());

        Map<String, String> attributes = new HashMap<>();
        attributes.put("email", request.getEmail());
        attributes.put("name", request.getFullName());
        attributes.put("custom:role", request.getRole().name());
        attributes.put("phone_number", request.getPhoneNumber());

        cognitoRequest.setUserAttributes(
            attributes.entrySet().stream()
                .map(e -> new AttributeType().withName(e.getKey()).withValue(e.getValue()))
                .toList()
        );

        try {
            cognitoClient.signUp(cognitoRequest);
            
            // Send verification code via SMS
            if (request.getPhoneNumber() != null) {
                sendVerificationSMS(request.getPhoneNumber());
            }

            // Send verification email
            sendVerificationEmail(request.getEmail());

            // Create local user (unverified)
            User user = new User();
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());
            user.setRole(request.getRole());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setVerified(false);
            userRepository.save(user);

            return new AuthResponse(null, user); // No token until verified

        } catch (UsernameExistsException e) {
            throw new RuntimeException("Email already registered");
        }
    }

    public AuthResponse verifyUser(VerificationRequest request) {
        try {
            ConfirmSignUpRequest confirmRequest = new ConfirmSignUpRequest()
                .withClientId(clientId)
                .withUsername(request.getEmail())
                .withConfirmationCode(request.getCode());

            cognitoClient.confirmSignUp(confirmRequest);

            // Update local user
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
            user.setVerified(true);
            userRepository.save(user);

            // Generate JWT token
            String token = jwtService.generateToken(user);
            return new AuthResponse(token, user);

        } catch (CodeMismatchException e) {
            throw new RuntimeException("Invalid verification code");
        }
    }

    public AuthResponse login(LoginRequest request) {
        try {
            // Check if user is verified
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isVerified()) {
                throw new RuntimeException("Please verify your account first");
            }

            AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                .withAuthFlow("ADMIN_NO_SRP_AUTH")
                .withUserPoolId(userPoolId)
                .withClientId(clientId)
                .withAuthParameters(Map.of(
                    "USERNAME", request.getEmail(),
                    "PASSWORD", request.getPassword()
                ));

            AdminInitiateAuthResult authResult = cognitoClient.adminInitiateAuth(authRequest);

            // Update last login
            user.setLastLoginAt(java.time.LocalDateTime.now());
            userRepository.save(user);

            String token = jwtService.generateToken(user);
            return new AuthResponse(token, user);

        } catch (NotAuthorizedException | UserNotFoundException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void resetPassword(String email) {
        try {
            // First, check if user exists and is verified
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (!user.isVerified()) {
                throw new RuntimeException("Please verify your account first");
            }

            // Generate reset code
            String resetCode = UUID.randomUUID().toString().substring(0, 8);

            // Store reset code in Cognito custom attributes
            AdminUpdateUserAttributesRequest updateRequest = new AdminUpdateUserAttributesRequest()
                .withUserPoolId(userPoolId)
                .withUsername(email)
                .withUserAttributes(
                    new AttributeType()
                        .withName("custom:resetCode")
                        .withValue(resetCode)
                );
            cognitoClient.adminUpdateUserAttributes(updateRequest);

            // Send reset code via email and SMS
            sendPasswordResetEmail(email, resetCode);
            if (user.getPhoneNumber() != null) {
                sendPasswordResetSMS(user.getPhoneNumber(), resetCode);
            }

        } catch (UserNotFoundException e) {
            throw new RuntimeException("User not found");
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