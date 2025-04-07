#!/bin/bash

# Vercel Setup Script for The Soul App

echo "🚀 Setting up Vercel deployment for The Soul App..."

# Check if Vercel CLI is installed
if ! command -v vercel &> /dev/null; then
    echo "❌ Vercel CLI is not installed. Please install it first."
    exit 1
fi

# Login to Vercel
echo "🔑 Logging in to Vercel..."
vercel login

# Initialize Vercel project
echo "📦 Initializing Vercel project..."
vercel init

# Create vercel.json configuration
echo "⚙️ Creating Vercel configuration..."
cat > vercel.json << EOL
{
  "version": 2,
  "builds": [
    {
      "src": "package.json",
      "use": "@vercel/static-build",
      "config": {
        "distDir": "build"
      }
    }
  ],
  "routes": [
    {
      "src": "/api/(.*)",
      "dest": "https://api.thesoulapp.com/api/$1"
    },
    {
      "src": "/(.*)",
      "dest": "/index.html"
    }
  ],
  "env": {
    "REACT_APP_API_URL": "https://api.thesoulapp.com",
    "REACT_APP_WS_URL": "wss://api.thesoulapp.com/ws"
  }
}
EOL

# Create environment variables file
echo "🔒 Creating environment variables..."
cat > .env.production << EOL
REACT_APP_API_URL=https://api.thesoulapp.com
REACT_APP_WS_URL=wss://api.thesoulapp.com/ws
REACT_APP_COGNITO_USER_POOL_ID=$COGNITO_USER_POOL_ID
REACT_APP_COGNITO_CLIENT_ID=$COGNITO_CLIENT_ID
REACT_APP_AWS_REGION=$AWS_REGION
EOL

# Deploy to Vercel
echo "🚀 Deploying to Vercel..."
vercel --prod

echo "✅ Vercel setup completed successfully!"
echo "📝 Next steps:"
echo "1. Configure custom domain in Vercel dashboard"
echo "2. Set up SSL certificate"
echo "3. Configure environment variables in Vercel dashboard"
echo "4. Enable automatic deployments from GitHub" 