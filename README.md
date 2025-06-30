# Phonebook Application

A secure Spring Boot application for managing phonebook entries with comprehensive input sanitization and SQL injection protection.

## Features

- ✅ **SQL Injection Protection**: Comprehensive input validation and sanitization
- ✅ **XSS Prevention**: HTML entity escaping and input validation
- ✅ **Secure Search**: Elasticsearch-powered search with sanitization
- ✅ **Database Security**: Parameterized queries and validation
- ✅ **Multi-environment Support**: Staging and production configurations

## Quick Start

### Local Development

```bash
# Clone the repository
git clone <repository-url>
cd phonebook

# Run the application
./gradlew bootRun
```

### Heroku Deployment

The application is configured for deployment to Heroku in two environments:

#### Automated Setup

```bash
# Initial Heroku setup
./setup-heroku.sh

# Deploy to staging
./deploy-staging.sh

# Deploy to production
./deploy-production.sh
```

#### Manual Setup

See [HEROKU_DEPLOYMENT.md](HEROKU_DEPLOYMENT.md) for detailed deployment instructions.

## Environment URLs

- **Staging**: https://phonebook-staging.herokuapp.com
- **Production**: https://phonebook-production.herokuapp.com

## Security Features

- Input sanitization and validation
- SQL injection prevention
- XSS protection
- Path traversal prevention
- Comprehensive error handling

## Technology Stack

- **Backend**: Spring Boot 3.5.3, Java 17
- **Database**: PostgreSQL with Liquibase migrations
- **Search**: Elasticsearch
- **Frontend**: Thymeleaf, Bootstrap 5
- **Build Tool**: Gradle
- **Deployment**: Heroku

## Documentation

- [Security Implementation](SANITIZATION.md)
- [Heroku Deployment Guide](HEROKU_DEPLOYMENT.md)
- [API Documentation](API_DOCUMENTATION.md)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under the MIT License.
