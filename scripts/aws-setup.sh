#!/bin/bash

# AWS Configuration Script for The Soul App

echo "🚀 Setting up AWS configuration for The Soul App..."

# Check if AWS CLI is installed
if ! command -v aws &> /dev/null; then
    echo "❌ AWS CLI is not installed. Please install it first."
    exit 1
fi

# Configure AWS credentials
echo "🔑 Configuring AWS credentials..."
aws configure

# Create S3 bucket for deployments
echo "📦 Creating S3 bucket for deployments..."
aws s3api create-bucket \
    --bucket soulapp-deployments \
    --region $AWS_REGION \
    --create-bucket-configuration LocationConstraint=$AWS_REGION

# Create DynamoDB table for assessments
echo "🗄️ Creating DynamoDB table for assessments..."
aws dynamodb create-table \
    --table-name Assessments \
    --attribute-definitions AttributeName=id,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

# Create SNS topics
echo "📢 Creating SNS topics..."
aws sns create-topic --name verification-topic --attributes FifoTopic=true
aws sns create-topic --name notification-topic --attributes FifoTopic=true

# Verify email in SES
echo "📧 Verifying email in SES..."
aws ses verify-email-identity --email-address $SES_SOURCE_EMAIL

# Create Cognito User Pool
echo "👥 Creating Cognito User Pool..."
aws cognito-idp create-user-pool \
    --pool-name soulapp-users \
    --auto-verified-attributes email \
    --schema \
        Name=email,Required=true,Mutable=true \
        Name=name,Required=true,Mutable=true \
        Name=phone_number,Required=false,Mutable=true

# Create Cognito App Client
echo "📱 Creating Cognito App Client..."
aws cognito-idp create-user-pool-client \
    --user-pool-id $COGNITO_USER_POOL_ID \
    --client-name soulapp-client \
    --no-generate-secret \
    --explicit-auth-flows ALLOW_USER_PASSWORD_AUTH ALLOW_REFRESH_TOKEN_AUTH

echo "✅ AWS configuration completed successfully!"
echo "📝 Please add the following to your GitHub repository secrets:"
echo "AWS_ACCESS_KEY_ID"
echo "AWS_SECRET_ACCESS_KEY"
echo "AWS_REGION"
echo "COGNITO_USER_POOL_ID"
echo "COGNITO_CLIENT_ID"
echo "SES_SOURCE_EMAIL"
echo "SNS_VERIFICATION_TOPIC"
echo "SNS_NOTIFICATION_TOPIC" 