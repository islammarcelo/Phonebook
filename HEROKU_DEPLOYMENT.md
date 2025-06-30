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

- **Staging**: `phonebook-staging`
- **Production**: `phonebook-production`

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
heroku create phonebook-staging

# Create production app
heroku create phonebook-production
```

#### 2. Set Buildpacks

```bash
# For both apps
heroku buildpacks:set heroku/gradle --app phonebook-staging
heroku buildpacks:set heroku/gradle --app phonebook-production
```

#### 3. Configure Environment Variables

```bash
# Staging
heroku config:set SPRING_PROFILES_ACTIVE=staging --app phonebook-staging

# Production
heroku config:set SPRING_PROFILES_ACTIVE=production --app phonebook-production
```

#### 4. Add Database Addons

```bash
# Staging - PostgreSQL Mini
heroku addons:create heroku-postgresql:mini --app phonebook-staging

# Production - PostgreSQL Standard
heroku addons:create heroku-postgresql:standard-0 --app phonebook-production
```

#### 5. Add Elasticsearch Addons

```bash
# Staging - Bonsai Sandbox
heroku addons:create bonsai:sandbox --app phonebook-staging

# Production - Bonsai Starter
heroku addons:create bonsai:starter --app phonebook-production
```

#### 6. Deploy Application

```bash
# Build and deploy
./gradlew clean build -x test
./gradlew herokuDeploy -PherokuAppName=phonebook-staging
./gradlew herokuDeploy -PherokuAppName=phonebook-production
```

#### 7. Run Database Migrations

```bash
# Staging
heroku run ./gradlew liquibaseUpdate --app phonebook-staging

# Production
heroku run ./gradlew liquibaseUpdate --app phonebook-production
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
heroku config:set DB_USERNAME=your_username --app phonebook-staging
heroku config:set DB_PASSWORD=your_password --app phonebook-staging

# Production
heroku config:set DB_USERNAME=your_username --app phonebook-production
heroku config:set DB_PASSWORD=your_password --app phonebook-production
```

## Monitoring and Logs

### View Application Logs

```bash
# Staging
heroku logs --tail --app phonebook-staging

# Production
heroku logs --tail --app phonebook-production
```

### Check Application Status

```bash
# Staging
heroku ps --app phonebook-staging

# Production
heroku ps --app phonebook-production
```

### Monitor Addons

```bash
# View all addons
heroku addons --app phonebook-staging
heroku addons --app phonebook-production
```

## Database Management

### Access PostgreSQL Console

```bash
# Staging
heroku pg:psql --app phonebook-staging

# Production
heroku pg:psql --app phonebook-production
```

### Database Backups

```bash
# Create backup
heroku pg:backups:capture --app phonebook-production

# Download backup
heroku pg:backups:download --app phonebook-production
```

## Troubleshooting

### Common Issues

1. **Build Failures**

   ```bash
   # Check build logs
   heroku builds --app phonebook-staging
   ```

2. **Database Connection Issues**

   ```bash
   # Check database status
   heroku pg:info --app phonebook-staging
   ```

3. **Memory Issues**
   ```bash
   # Check dyno memory usage
   heroku logs --app phonebook-staging | grep "Memory"
   ```

### Scaling

```bash
# Scale web dynos
heroku ps:scale web=1 --app phonebook-staging
heroku ps:scale web=2 --app phonebook-production
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
