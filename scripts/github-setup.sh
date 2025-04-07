#!/bin/bash

# GitHub Actions Setup Script for The Soul App

echo "🚀 Setting up GitHub Actions for The Soul App..."

# Check if GitHub CLI is installed
if ! command -v gh &> /dev/null; then
    echo "❌ GitHub CLI is not installed. Please install it first."
    exit 1
fi

# Authenticate with GitHub
echo "🔑 Authenticating with GitHub..."
gh auth login

# Create repository
echo "📦 Creating GitHub repository..."
gh repo create thesoulapp-backend --public --description "Backend for The Soul App" --source=. --remote=origin

# Set up repository secrets
echo "🔐 Setting up repository secrets..."
read -p "Enter AWS Access Key ID: " AWS_ACCESS_KEY_ID
read -p "Enter AWS Secret Access Key: " AWS_SECRET_ACCESS_KEY
read -p "Enter AWS Region: " AWS_REGION
read -p "Enter Cognito User Pool ID: " COGNITO_USER_POOL_ID
read -p "Enter Cognito Client ID: " COGNITO_CLIENT_ID
read -p "Enter SES Source Email: " SES_SOURCE_EMAIL
read -p "Enter SNS Verification Topic ARN: " SNS_VERIFICATION_TOPIC
read -p "Enter SNS Notification Topic ARN: " SNS_NOTIFICATION_TOPIC

# Add secrets to repository
echo "🔒 Adding secrets to repository..."
gh secret set AWS_ACCESS_KEY_ID -b"$AWS_ACCESS_KEY_ID"
gh secret set AWS_SECRET_ACCESS_KEY -b"$AWS_SECRET_ACCESS_KEY"
gh secret set AWS_REGION -b"$AWS_REGION"
gh secret set COGNITO_USER_POOL_ID -b"$COGNITO_USER_POOL_ID"
gh secret set COGNITO_CLIENT_ID -b"$COGNITO_CLIENT_ID"
gh secret set SES_SOURCE_EMAIL -b"$SES_SOURCE_EMAIL"
gh secret set SNS_VERIFICATION_TOPIC -b"$SNS_VERIFICATION_TOPIC"
gh secret set SNS_NOTIFICATION_TOPIC -b"$SNS_NOTIFICATION_TOPIC"

# Enable GitHub Actions
echo "⚙️ Enabling GitHub Actions..."
gh api repos/$(gh repo view --json nameWithOwner -q .nameWithOwner)/actions/permissions --method PUT -f enabled=true

# Create main branch
echo "🌳 Creating main branch..."
git checkout -b main
git add .
git commit -m "Initial commit"
git push -u origin main

echo "✅ GitHub Actions setup completed successfully!"
echo "📝 Next steps:"
echo "1. Wait for the initial workflow to complete"
echo "2. Check the Elastic Beanstalk environment status"
echo "3. Verify the deployment in AWS" 