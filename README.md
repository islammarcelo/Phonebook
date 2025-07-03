# Phonebook

A Spring Boot application for managing phonebook entries with Elasticsearch integration.

## Features

- CRUD operations for phonebook entries
- Search functionality with Elasticsearch
- Input sanitization and validation
- Responsive Bootstrap UI
- Environment-specific configurations

## Dynamic Titles

The application title changes based on the Spring profile:

- **Development** (default): "Phonebook - Development"
- **Staging**: "Phonebook - Staging"
- **Production**: "Phonebook - Production"

## External Services Deployment

### Elasticsearch on Railway

Elasticsearch is deployed on Railway for search functionality:

1. **Deploy to Railway:**

   - Go to [Railway](https://railway.app/)
   - Create a new project
   - Add Elasticsearch service from the marketplace
   - Note the connection URL provided by Railway

2. **Configure Environment Variables:**

   ```bash
   # For staging
   heroku config:set ELASTICSEARCH_URI=your_railway_elasticsearch_url --app islam-app-stage

   # For production
   heroku config:set ELASTICSEARCH_URL=your_railway_elasticsearch_url --app islam-app-prod
   ```

### PostgreSQL on Supabase

PostgreSQL database is deployed on Supabase:

1. **Deploy to Supabase:**

   - Go to [Supabase](https://supabase.com/)
   - Create a new project
   - Note the database connection details from Settings > Database

2. **Configure Environment Variables:**

   ```bash
   # For staging
   heroku config:set JDBC_DATABASE_URL=your_supabase_jdbc_url --app islam-app-stage
   heroku config:set JDBC_DATABASE_USERNAME=your_supabase_username --app islam-app-stage
   heroku config:set JDBC_DATABASE_PASSWORD=your_supabase_password --app islam-app-stage

   # For production
   heroku config:set JDBC_DATABASE_URL=your_supabase_jdbc_url --app islam-app-prod
   heroku config:set JDBC_DATABASE_USERNAME=your_supabase_username --app islam-app-prod
   heroku config:set JDBC_DATABASE_PASSWORD=your_supabase_password --app islam-app-prod
   ```

3. **Database Migration:**
   - The application uses Liquibase for database migrations
   - Migrations run automatically on application startup
   - Check `src/main/resources/db/changelog/` for migration scripts

## Running the Application

### Local Development

```bash
./gradlew bootRun
```

This will use the default profile and show "Phonebook - Development"

### With Specific Profile

```bash
# Staging
SPRING_PROFILES_ACTIVE=staging ./gradlew bootRun

# Production
SPRING_PROFILES_ACTIVE=production ./gradlew bootRun
```

### Heroku Deployment

The application is configured for Heroku deployment with environment-specific settings:

- **Staging**: `islam-app-stage` with `SPRING_PROFILES_ACTIVE=staging`
- **Production**: `islam-app-prod` with `SPRING_PROFILES_ACTIVE=production`

Use the provided scripts:

```bash
# Setup Heroku
./setup-heroku.sh

# Deploy to staging
./deploy-staging.sh

# Deploy to production
./deploy-production.sh
```

## Configuration

The application uses different configuration files based on the active profile:

- `application-default.yml` - Development settings
- `application-staging.yml` - Staging environment
- `application-production.yml` - Production environment

Each profile has its own database configuration, logging levels, and application title.

## Environment Variables

### Required for Staging/Production:

- `JDBC_DATABASE_URL` - Supabase PostgreSQL connection URL
- `JDBC_DATABASE_USERNAME` - Supabase database username
- `JDBC_DATABASE_PASSWORD` - Supabase database password
- `ELASTICSEARCH_URI` (staging) / `ELASTICSEARCH_URL` (production) - Railway Elasticsearch URL
- `SPRING_PROFILES_ACTIVE` - Active Spring profile (staging/production)
