package com.thesoulapp.auth.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoService {

    private final AWSCognitoIdentityProvider cognitoClient;

    @Value("${aws.cognito.userPoolId}")
    private String userPoolId;

    @Value("${aws.cognito.clientId}")
    private String clientId;

    /**
     * Sign up a new user in Cognito
     */
    public String signUp(String email, String password, String name) {
        try {
            // Prepare attributes
            AttributeType emailAttr = new AttributeType().withName("email").withValue(email);
            AttributeType nameAttr = new AttributeType().withName("name").withValue(name);

            // Create sign up request
            SignUpRequest signUpRequest = new SignUpRequest()
                    .withClientId(clientId)
                    .withUsername(email)
                    .withPassword(password)
                    .withUserAttributes(emailAttr, nameAttr);

            SignUpResult result = cognitoClient.signUp(signUpRequest);
            return result.getUserSub(); // Return the Cognito user ID
        } catch (Exception e) {
            throw new RuntimeException("Error during Cognito signup: " + e.getMessage(), e);
        }
    }

    /**
     * Confirm a user signup with the verification code
     */
    public void confirmSignUp(String email, String confirmationCode) {
        try {
            ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
                    .withClientId(clientId)
                    .withUsername(email)
                    .withConfirmationCode(confirmationCode);

            cognitoClient.confirmSignUp(confirmSignUpRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error during confirmation: " + e.getMessage(), e);
        }
    }

    /**
     * Initiate forgot password flow
     */
    public void forgotPassword(String email) {
        try {
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
                    .withClientId(clientId)
                    .withUsername(email);

            cognitoClient.forgotPassword(forgotPasswordRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error during forgot password: " + e.getMessage(), e);
        }
    }

    /**
     * Complete forgot password with confirmation code and new password
     */
    public void confirmForgotPassword(String email, String confirmationCode, String newPassword) {
        try {
            ConfirmForgotPasswordRequest confirmForgotPasswordRequest = new ConfirmForgotPasswordRequest()
                    .withClientId(clientId)
                    .withUsername(email)
                    .withConfirmationCode(confirmationCode)
                    .withPassword(newPassword);

            cognitoClient.confirmForgotPassword(confirmForgotPasswordRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error during confirm forgot password: " + e.getMessage(), e);
        }
    }

    /**
     * Authenticate user and return tokens
     */
    public AdminInitiateAuthResult authenticate(String email, String password) {
        try {
            Map<String, String> authParams = new HashMap<>();
            authParams.put("USERNAME", email);
            authParams.put("PASSWORD", password);

            AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .withClientId(clientId)
                    .withUserPoolId(userPoolId)
                    .withAuthParameters(authParams);

            return cognitoClient.adminInitiateAuth(authRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error during authentication: " + e.getMessage(), e);
        }
    }

    /**
     * Refresh tokens using refresh token
     */
    public AdminInitiateAuthResult refreshTokens(String refreshToken) {
        try {
            Map<String, String> authParams = new HashMap<>();
            authParams.put("REFRESH_TOKEN", refreshToken);

            AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                    .withClientId(clientId)
                    .withUserPoolId(userPoolId)
                    .withAuthParameters(authParams);

            return cognitoClient.adminInitiateAuth(authRequest);
        } catch (Exception e) {
            throw new RuntimeException("Error during token refresh: " + e.getMessage(), e);
        }
    }
} 