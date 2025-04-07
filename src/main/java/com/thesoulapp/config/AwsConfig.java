package com.thesoulapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.access-key}")
    private String accessKey;

    @Value("${aws.secret-key}")
    private String secretKey;

    private AwsBasicCredentials awsCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean
    public CognitoIdentityProviderClient cognitoClient() {
        return CognitoIdentityProviderClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
            .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
            .build();
    }

    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
            .build();
    }

    @Bean
    public SnsClient snsClient() {
        return SnsClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
            .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
            .build();
    }

    @Bean
    public CloudWatchClient cloudWatchClient() {
        return CloudWatchClient.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
            .build();
    }
} 