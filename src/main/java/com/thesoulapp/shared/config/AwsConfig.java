package com.thesoulapp.shared.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodb.AmazonDynamoDB;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AwsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.dynamodb.endpoint:}")
    private String dynamoDbEndpoint;

    private AWSStaticCredentialsProvider getCredentialsProvider() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    @Bean
    public AWSCognitoIdentityProvider cognitoClient() {
        return AWSCognitoIdentityProviderClientBuilder.standard()
                .withRegion(region)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonSNS snsClient() {
        return AmazonSNSClientBuilder.standard()
                .withRegion(region)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonSimpleEmailService sesClient() {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(region)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    @Profile("!local") // Use this for non-local environments
    public AmazonDynamoDB dynamoDbClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    @Profile("local") // Use this for local development
    public AmazonDynamoDB localDynamoDbClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(dynamoDbEndpoint, region)
                )
                .withCredentials(getCredentialsProvider())
                .build();
    }
} 