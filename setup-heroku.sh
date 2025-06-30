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

# Create staging app
echo "📦 Creating staging application..."
heroku create phonebook-staging --buildpack heroku/gradle

# Create production app
echo "📦 Creating production application..."
heroku create phonebook-production --buildpack heroku/gradle

# Set environment variables for staging
echo "⚙️ Configuring staging environment..."
heroku config:set SPRING_PROFILES_ACTIVE=staging --app phonebook-staging

# Set environment variables for production
echo "⚙️ Configuring production environment..."
heroku config:set SPRING_PROFILES_ACTIVE=production --app phonebook-production

# Add PostgreSQL addons
echo "🗄️ Adding PostgreSQL addons..."
heroku addons:create heroku-postgresql:mini --app phonebook-staging
heroku addons:create heroku-postgresql:standard-0 --app phonebook-production

# Add Elasticsearch addons
echo "🔍 Adding Elasticsearch addons..."
heroku addons:create bonsai:sandbox --app phonebook-staging
heroku addons:create bonsai:starter --app phonebook-production

echo "✅ Heroku setup completed!"
echo ""
echo "📋 Next steps:"
echo "1. Set database credentials:"
echo "   heroku config:set DB_USERNAME=your_username --app phonebook-staging"
echo "   heroku config:set DB_PASSWORD=your_password --app phonebook-staging"
echo "   heroku config:set DB_USERNAME=your_username --app phonebook-production"
echo "   heroku config:set DB_PASSWORD=your_password --app phonebook-production"
echo ""
echo "2. Deploy applications:"
echo "   ./deploy-staging.sh"
echo "   ./deploy-production.sh"
echo ""
echo "3. Check application status:"
echo "   heroku open --app phonebook-staging"
echo "   heroku open --app phonebook-production" 