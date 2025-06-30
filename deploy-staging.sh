#!/bin/bash

# Deploy to Heroku Staging Environment
echo "🚀 Deploying to Heroku Staging Environment..."

# Set the app name for staging
export HEROKU_APP_NAME="phonebook-staging"

# Build the application
echo "📦 Building application..."
./gradlew clean build -x test

# Deploy to Heroku
echo "🌐 Deploying to Heroku..."
./gradlew herokuDeploy -PherokuAppName=$HEROKU_APP_NAME

# Set environment variables for staging
echo "⚙️ Setting environment variables..."
heroku config:set SPRING_PROFILES_ACTIVE=staging --app $HEROKU_APP_NAME

# Add PostgreSQL addon if not exists
echo "🗄️ Setting up PostgreSQL..."
heroku addons:create heroku-postgresql:mini --app $HEROKU_APP_NAME

# Add Elasticsearch addon if not exists
echo "🔍 Setting up Elasticsearch..."
heroku addons:create bonsai:sandbox --app $HEROKU_APP_NAME

# Run database migrations
echo "🔄 Running database migrations..."
heroku run ./gradlew liquibaseUpdate --app $HEROKU_APP_NAME

echo "✅ Staging deployment completed!"
echo "🌐 Staging URL: https://$HEROKU_APP_NAME.herokuapp.com" 