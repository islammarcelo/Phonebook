#!/bin/bash

# Deploy to Heroku Staging Environment
echo "🚀 Deploying to Heroku Staging Environment..."

# Set the app name for staging
export HEROKU_APP_NAME="islam-app-stage"

# Build the application (skip tests to avoid database issues)
echo "📦 Building application..."
./gradlew clean build -x test

# Deploy to Heroku using standard git method (from develop branch)
echo "🌐 Deploying to Heroku from develop branch..."
git push https://git.heroku.com/$HEROKU_APP_NAME.git develop

# Set environment variables for staging
echo "⚙️ Setting environment variables..."
heroku config:set SPRING_PROFILES_ACTIVE=staging --app $HEROKU_APP_NAME

echo "✅ Staging deployment completed!"
echo "🌐 Staging URL: https://$HEROKU_APP_NAME.herokuapp.com"
echo "📝 Note: Database migrations will run automatically when the app starts" 