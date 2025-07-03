#!/bin/bash

# Heroku Setup Script for Phonebook Application
echo "🚀 Setting up Heroku deployment for Phonebook Application..."

# Check if Heroku CLI is installed
if ! command -v heroku &> /dev/null; then
    echo "❌ Heroku CLI is not installed. Please install it first:"
    echo "   macOS: brew tap heroku/brew && brew install heroku"
    echo "   Linux: curl https://cli-assets.heroku.com/install.sh | sh"
    echo "   Windows: Download from https://devcenter.heroku.com/articles/heroku-cli"
    exit 1
fi

# Check if user is logged in
if ! heroku auth:whoami &> /dev/null; then
    echo "🔐 Please login to Heroku first:"
    heroku login
fi

echo "✅ Heroku CLI is ready!"

# Note: Apps are already created manually
echo "📦 Apps already created: islam-app-stage and islam-app-prod"

# Set environment variables for staging
echo "⚙️ Configuring staging environment..."
heroku config:set SPRING_PROFILES_ACTIVE=staging --app islam-app-stage

# Set environment variables for production
echo "⚙️ Configuring production environment..."
heroku config:set SPRING_PROFILES_ACTIVE=production --app islam-app-prod
