#!/bin/bash

# Deploy to Heroku Production Environment
echo "🚀 Deploying to Heroku Production Environment..."

# Set the app name for production
export HEROKU_APP_NAME="phonebook-production"

# Build the application
echo "📦 Building application..."
./gradlew clean build -x test

# Deploy to Heroku
echo "🌐 Deploying to Heroku..."
./gradlew herokuDeploy -PherokuAppName=$HEROKU_APP_NAME

# Set environment variables for production
echo "⚙️ Setting environment variables..."
heroku config:set SPRING_PROFILES_ACTIVE=production --app $HEROKU_APP_NAME

# Add PostgreSQL addon if not exists
echo "🗄️ Setting up PostgreSQL..."
heroku addons:create heroku-postgresql:standard-0 --app $HEROKU_APP_NAME

# Add Elasticsearch addon if not exists
echo "🔍 Setting up Elasticsearch..."
heroku addons:create bonsai:starter --app $HEROKU_APP_NAME

# Run database migrations
echo "🔄 Running database migrations..."
heroku run ./gradlew liquibaseUpdate --app $HEROKU_APP_NAME

echo "✅ Production deployment completed!"
echo "🌐 Production URL: https://$HEROKU_APP_NAME.herokuapp.com" 