#!/bin/bash

# Deploy to Heroku Production Environment
echo "🚀 Deploying to Heroku Production Environment..."

# Set the app name for production
export HEROKU_APP_NAME="islam-app-prod"

# Build the application
echo "📦 Building application..."
./gradlew clean build -x test

# Deploy to Heroku using standard git method (from main branch)
echo "🌐 Deploying to Heroku from main branch..."
git push https://git.heroku.com/$HEROKU_APP_NAME.git main

# Set environment variables for production
echo "⚙️ Setting environment variables..."
heroku config:set SPRING_PROFILES_ACTIVE=production --app $HEROKU_APP_NAME

echo "✅ Production deployment completed!"
echo "🌐 Production URL: https://$HEROKU_APP_NAME.herokuapp.com"
echo "📝 Note: Database migrations will run automatically when the app starts" 