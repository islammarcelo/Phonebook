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

# # Add PostgreSQL addons
# echo "🗄️ Adding PostgreSQL addons..."
# heroku addons:create heroku-postgresql:mini --app islam-app-stage
# heroku addons:create heroku-postgresql:standard-0 --app islam-app-prod


# Add Elasticsearch addons
echo "🔍 Adding Elasticsearch addons..."
heroku addons:create bonsai:sandbox --app islam-app-stage
heroku addons:create bonsai:sandbox --app islam-app-prod

echo "✅ Heroku setup completed!"
echo ""
echo "📋 Next steps:"
echo "1. Set Supabase database credentials:"
echo "   heroku config:set DATABASE_URL=your_supabase_url --app islam-app-stage"
echo "   heroku config:set DB_USERNAME=postgres --app islam-app-stage"
echo "   heroku config:set DB_PASSWORD=your_supabase_password --app islam-app-stage"
echo "   heroku config:set DATABASE_URL=your_supabase_url --app islam-app-prod"
echo "   heroku config:set DB_USERNAME=postgres --app islam-app-prod"
echo "   heroku config:set DB_PASSWORD=your_supabase_password --app islam-app-prod"
echo ""
echo "2. Deploy applications:"
echo "   ./deploy-staging.sh"
echo "   ./deploy-production.sh"
echo ""
echo "3. Check application status:"
echo "   heroku open --app islam-app-stage"
echo "   heroku open --app islam-app-prod" 