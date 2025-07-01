# Heroku Deployment Guide

This guide explains how to deploy the Phonebook application to Heroku in both staging and production environments.

## Prerequisites

1. **Heroku CLI** installed and authenticated
2. **Git** repository set up
3. **Java 17** installed locally for building

## Environment Setup

### 1. Install Heroku CLI

```bash
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# Download from https://devcenter.heroku.com/articles/heroku-cli

# Linux
curl https://cli-assets.heroku.com/install.sh | sh
```

### 2. Login to Heroku

```bash
heroku login
```

### 3. Verify Heroku CLI

```bash
heroku --version
```

## Application Structure

The application is configured for two environments:

- **Staging**: `islam-app-stage`
- **Production**: `islam-app-prod`

### Configuration Files

- `application-staging.yml` - Staging environment configuration
- `application-production.yml` - Production environment configuration
- `Procfile` - Heroku process definition
- `system.properties` - Java version specification

## Deployment Process

### Option 1: Automated Deployment (Recommended)

#### Deploy to Staging

```bash
./deploy-staging.sh
```

#### Deploy to Production

```bash
./deploy-production.sh
```

### Option 2: Manual Deployment

#### 1. Create Heroku Apps

```bash
# Create staging app
heroku create islam-app-stage

# Create production app
heroku create islam-app-prod
```

#### 2. Set Buildpacks

```bash
# For both apps
heroku buildpacks:set heroku/gradle --app islam-app-stage
heroku buildpacks:set heroku/gradle --app islam-app-prod
```

#### 3. Configure Environment Variables

```bash
# Staging
heroku config:set SPRING_PROFILES_ACTIVE=staging --app islam-app-stage

# Production
heroku config:set SPRING_PROFILES_ACTIVE=production --app islam-app-prod
```

#### 4. Add Database Addons

```bash
# Staging - PostgreSQL Mini
heroku addons:create heroku-postgresql:mini --app islam-app-stage

# Production - PostgreSQL Standard
heroku addons:create heroku-postgresql:standard-0 --app islam-app-prod
```

#### 5. Add Elasticsearch Addons

```bash
# Staging - Bonsai Sandbox
heroku addons:create bonsai:sandbox --app islam-app-stage

# Production - Bonsai Starter
heroku addons:create bonsai:starter --app islam-app-prod
```

#### 6. Deploy Application

```bash
# Build and deploy
./gradlew clean build -x test
./gradlew herokuDeploy -PherokuAppName=islam-app-stage
./gradlew herokuDeploy -PherokuAppName=islam-app-prod
```

#### 7. Run Database Migrations

```bash
# Staging
heroku run ./gradlew liquibaseUpdate --app islam-app-stage

# Production
heroku run ./gradlew liquibaseUpdate --app islam-app-prod
```

## Environment Variables

### Required Environment Variables

The application expects these environment variables to be set:

- `SPRING_PROFILES_ACTIVE` - Environment profile (staging/production)
- `DATABASE_URL` - PostgreSQL connection URL (auto-set by Heroku)
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `ELASTICSEARCH_URL` - Elasticsearch connection URL (auto-set by Heroku)
- `PORT` - Server port (auto-set by Heroku)

### Setting Environment Variables

```bash
# Staging
heroku config:set DB_USERNAME=your_username --app islam-app-stage
heroku config:set DB_PASSWORD=your_password --app islam-app-stage

# Production
heroku config:set DB_USERNAME=your_username --app islam-app-prod
heroku config:set DB_PASSWORD=your_password --app islam-app-prod
```

## Monitoring and Logs

### View Application Logs

```bash
# Staging
heroku logs --tail --app islam-app-stage

# Production
heroku logs --tail --app islam-app-prod
```

### Check Application Status

```bash
# Staging
heroku ps --app islam-app-stage

# Production
heroku ps --app islam-app-prod
```

### Monitor Addons

```bash
# View all addons
heroku addons --app islam-app-stage
heroku addons --app islam-app-prod
```

## Database Management

### Access PostgreSQL Console

```bash
# Staging
heroku pg:psql --app islam-app-stage

# Production
heroku pg:psql --app islam-app-prod
```

### Database Backups

```bash
# Create backup
heroku pg:backups:capture --app islam-app-prod

# Download backup
heroku pg:backups:download --app islam-app-prod
```

## Troubleshooting

### Common Issues

1. **Build Failures**

   ```bash
   # Check build logs
   heroku builds --app islam-app-stage
   ```

2. **Database Connection Issues**

   ```bash
   # Check database status
   heroku pg:info --app islam-app-stage
   ```

3. **Memory Issues**
   ```bash
   # Check dyno memory usage
   heroku logs --app islam-app-stage | grep "Memory"
   ```

### Scaling

```bash
# Scale web dynos
heroku ps:scale web=1 --app islam-app-stage
heroku ps:scale web=2 --app islam-app-prod
```

## Security Considerations

### Environment-Specific Security

- **Staging**: More verbose logging, SQL queries visible
- **Production**: Minimal logging, SQL queries disabled, health endpoints restricted

### SSL/TLS

Heroku automatically provides SSL certificates for all apps.

### Database Security

- Use strong passwords
- Regularly rotate credentials
- Enable database backups
- Monitor access logs

## Cost Optimization

### Staging Environment

- PostgreSQL Mini: $5/month
- Bonsai Sandbox: Free tier
- Web Dyno: Free tier (sleeps after 30 minutes)

### Production Environment

- PostgreSQL Standard-0: $50/month
- Bonsai Starter: $10/month
- Web Dyno: $7/month per dyno

## Continuous Deployment

### GitHub Integration

1. Connect GitHub repository to Heroku
2. Enable automatic deploys
3. Set up review apps for pull requests

### CI/CD Pipeline

Consider setting up GitHub Actions or similar CI/CD tools for automated testing and deployment.

## Support

For Heroku-specific issues:

- [Heroku Dev Center](https://devcenter.heroku.com/)
- [Heroku Support](https://help.heroku.com/)

For application-specific issues:

- Check application logs
- Review environment configuration
- Verify database connectivity
