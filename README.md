# The Soul App Backend

A Spring Boot backend application for The Soul App, providing therapy and mental health services.

## 🚀 Features

- User Authentication & Authorization
- Therapist-Patient Matching
- Assessment Management
- Session Scheduling
- Payment Processing
- Blog & Content Management
- Avatar & Gamification System
- Notification System

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Security
- AWS Services:
  - Cognito (Authentication)
  - S3 (File Storage)
  - SES (Email Service)
  - SNS (SMS Notifications)
  - DynamoDB (NoSQL Database)
  - CloudWatch (Monitoring)
- MySQL (RDS)
- JWT Authentication
- Gradle Build System

## 📋 Prerequisites

- Java 21
- Gradle
- AWS Account
- MySQL Database
- Docker (optional)

## 🔧 Configuration

1. Clone the repository:
```bash
git clone https://github.com/yourusername/thesoulapp-backend.git
```

2. Configure AWS credentials in `application.yml`:
```yaml
aws:
  region: your-region
  access-key: your-access-key
  secret-key: your-secret-key
```

3. Set up environment variables:
```bash
export AWS_REGION=your-region
export AWS_ACCESS_KEY=your-access-key
export AWS_SECRET_KEY=your-secret-key
export COGNITO_USER_POOL_ID=your-user-pool-id
export COGNITO_CLIENT_ID=your-client-id
export SES_SOURCE_EMAIL=your-verified-email
export SNS_VERIFICATION_TOPIC=topic-arn
export SNS_NOTIFICATION_TOPIC=topic-arn
export JWT_SECRET=your-secure-secret
```

## 🏗 Building the Project

```bash
./gradlew build
```

## 🚀 Running the Application

```bash
./gradlew bootRun
```

## 📦 Docker Deployment

```bash
docker build -t thesoulapp-backend .
docker run -p 8080:8080 thesoulapp-backend
```

## 🔐 Security

- JWT-based authentication
- Role-based access control
- Rate limiting
- CORS configuration
- SSL/TLS encryption
- Input validation
- SQL injection prevention

## 📈 Monitoring

- AWS CloudWatch integration
- Health endpoints
- Metrics collection
- Log aggregation

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- [Manuel Ernesto Murillo Arce] - Lead Developer
- [Ernesto Murillo] - Backend Developer
- [Manuel Erneeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee] - DevOps Engineer

## 📞 Support

For support, email support@thesoulapp.com or join our Slack channel.

# Install AWS CLI if not already installed
# Configure AWS credentials
aws configure

# Run the AWS setup script
./scripts/aws-setup.sh

# Initialize and apply Terraform
cd terraform
terraform init
terraform plan
terraform apply 