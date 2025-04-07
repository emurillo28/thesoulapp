#!/bin/bash

# The Soul App Setup Script

echo "🚀 Setting up The Soul App project..."

# Check prerequisites
echo "🔍 Checking prerequisites..."
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 21 first."
    exit 1
fi

if ! command -v gradle &> /dev/null; then
    echo "❌ Gradle is not installed. Please install Gradle first."
    exit 1
fi

if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

# Create necessary directories
echo "📁 Creating project structure..."
mkdir -p src/main/java/com/thesoulapp/{config,controller,service,repository,model,security,util}
mkdir -p src/main/resources
mkdir -p src/test/java/com/thesoulapp
mkdir -p .github/workflows
mkdir -p scripts
mkdir -p terraform

# Initialize Gradle project
echo "📦 Initializing Gradle project..."
gradle init --type java-application

# Create initial files
echo "📝 Creating initial files..."
touch src/main/resources/application.yml
touch src/main/resources/application-dev.yml
touch src/main/resources/application-prod.yml

# Set up Git
echo "🔧 Setting up Git..."
git init
git add .
git commit -m "Initial commit"

# Make scripts executable
echo "🔨 Making scripts executable..."
chmod +x scripts/*.sh

echo "✅ Project setup completed successfully!"
echo "📝 Next steps:"
echo "1. Configure AWS credentials"
echo "2. Set up GitHub repository"
echo "3. Configure environment variables"
echo "4. Run the application"
echo "5. Deploy to production"

# Display help message
echo "
To get started:

1. AWS Setup:
   ./scripts/aws-setup.sh

2. GitHub Setup:
   ./scripts/github-setup.sh

3. Vercel Setup:
   ./scripts/vercel-setup.sh

4. Infrastructure Setup:
   cd terraform
   terraform init
   terraform plan
   terraform apply

5. Run the application:
   ./gradlew bootRun

6. Build and test:
   ./gradlew build
   ./gradlew test

For more information, check the README.md file.
" 