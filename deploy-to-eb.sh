#!/bin/bash

# Deployment script for AWS Elastic Beanstalk

# Exit on any error
set -e

# Variables
APP_NAME="thesoulapp"
ENV_NAME="production"
REGION="us-east-1"  # Change as needed
PACKAGE_NAME="${APP_NAME}.jar"

echo "=== Building the application ==="
./mvnw clean package -DskipTests

echo "=== Creating deployment package ==="
mkdir -p target/deploy
cp target/*.jar target/deploy/${PACKAGE_NAME}
cp -r src/main/resources/.ebextensions target/deploy/
cd target/deploy

echo "=== Initializing Elastic Beanstalk if not already done ==="
if ! aws elasticbeanstalk describe-applications --application-names ${APP_NAME} --region ${REGION} &> /dev/null; then
    echo "Creating new Elastic Beanstalk application..."
    aws elasticbeanstalk create-application --application-name ${APP_NAME} --region ${REGION}
fi

echo "=== Creating new application version ==="
VERSION="v$(date +%Y%m%d%H%M%S)"
zip -r ${APP_NAME}-${VERSION}.zip ${PACKAGE_NAME} .ebextensions
aws s3 mb s3://${APP_NAME}-elasticbeanstalk-deployment --region ${REGION} || true
aws s3 cp ${APP_NAME}-${VERSION}.zip s3://${APP_NAME}-elasticbeanstalk-deployment/

aws elasticbeanstalk create-application-version \
    --application-name ${APP_NAME} \
    --version-label ${VERSION} \
    --source-bundle S3Bucket="${APP_NAME}-elasticbeanstalk-deployment",S3Key="${APP_NAME}-${VERSION}.zip" \
    --region ${REGION}

echo "=== Checking if environment exists ==="
if aws elasticbeanstalk describe-environments --application-name ${APP_NAME} --environment-names ${ENV_NAME} --region ${REGION} | grep -q ${ENV_NAME}; then
    echo "Updating existing environment..."
    aws elasticbeanstalk update-environment \
        --application-name ${APP_NAME} \
        --environment-name ${ENV_NAME} \
        --version-label ${VERSION} \
        --region ${REGION}
else
    echo "Creating new environment..."
    aws elasticbeanstalk create-environment \
        --application-name ${APP_NAME} \
        --environment-name ${ENV_NAME} \
        --solution-stack-name "64bit Amazon Linux 2 v3.5.0 running Corretto 17" \
        --version-label ${VERSION} \
        --region ${REGION} \
        --option-settings file://elasticbeanstalk-config.json
fi

echo "=== Deployment initiated ==="
echo "You can monitor the deployment status in the AWS Elastic Beanstalk console" 