#!/bin/bash

# Deploy to Heroku Staging Environment
echo "🚀 Deploying to Heroku Staging Environment..."

# Set the app name for staging
export HEROKU_APP_NAME="islam-app-stage"

# Build the application
echo "📦 Building application..."
./gradlew clean build -x test

# Deploy to Heroku using standard git method (from main branch)
echo "🌐 Deploying to Heroku from develop branch..."
git push https://git.heroku.com/$HEROKU_APP_NAME.git main

# Set environment variables for staging
echo "⚙️ Setting environment variables..."
heroku config:set SPRING_PROFILES_ACTIVE=staging --app $HEROKU_APP_NAME

echo "✅ Staging deployment completed!"
echo "🌐 Staging URL:https://islam-app-stage-76b1761c7433.herokuapp.com/phonebook"
echo "📝 Note: Database migrations will run automatically when the app starts" 