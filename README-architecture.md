# Spotify Application Architecture

## Overview
This document describes the architecture of the Spotify User Profile Viewer application, which is a web application that allows users to view their Spotify profile information and top artists after authenticating with Spotify's OAuth system.

## Architecture Diagram
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│  Web Browser    │────▶│  Spring Boot    │────▶│  Spotify API    │
│  (React UI)     │◀────│  Application    │◀────│                 │
│                 │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                               │  ▲
                               │  │
                               ▼  │
                        ┌─────────────────┐
                        │                 │
                        │  Monitoring     │
                        │  (Prometheus/   │
                        │   Grafana)      │
                        │                 │
                        └─────────────────┘
```

## Technology Stack
- **Backend**: Spring Boot 3.5.0
- **Frontend**: React 18 with Bootstrap 5.3.0
- **API Integration**: Spotify Web API
- **Containerization**: Docker
- **Monitoring**: Prometheus and Grafana
- **Reverse Proxy** (Production): Nginx

## Component Architecture

### 1. Backend (Spring Boot)

#### 1.1 Application Layer
- **SpotifyApplication.java**: Main entry point for the Spring Boot application
- Uses Spring Boot's auto-configuration and component scanning

#### 1.2 Configuration Layer
- **AuthConfig.java**: Configuration properties for OAuth authentication
- **SpotifyConfig.java**: Configuration properties for Spotify API
- **application.properties**: Application-wide configuration settings

#### 1.3 Controller Layer
- **OAuthController.java**: REST endpoints for OAuth authentication
  - `/api/oauth/authorize`: Initiates Spotify authorization flow
  - `/api/oauth/callback`: Handles OAuth callback from Spotify
  - `/api/oauth/bearer-token`: Returns the current bearer token
  - `/api/oauth/token`: Gets an access token using client credentials flow
- **UserController.java**: REST endpoints for user data
  - `/api/users/profile`: Returns the user's Spotify profile
  - `/api/users/tracks`: Returns the user's top artists

#### 1.4 Service Layer
- **OAuthService.java**: Business logic for OAuth authentication
  - Handles token exchange with Spotify
  - Manages bearer tokens
  - Makes authenticated requests to Spotify API
- **UserServer.java**: Business logic for user data
  - Fetches user profile from Spotify
  - Fetches user's top artists from Spotify

#### 1.5 Model Layer
- **SpotifyUser.java**: Data model for Spotify user profile
- **SpotifyShowsResponse.java**: Data model for Spotify top artists response

### 2. Frontend (React)

#### 2.1 Entry Point
- **index.html**: HTML template with React mount point
- Loads React, ReactDOM, and Bootstrap

#### 2.2 React Components
- **App.js**: Main React application
  - **UserProfile**: Component for displaying user profile
  - **UserTracks**: Component for displaying user's top artists

### 3. Deployment Architecture

#### 3.1 Development Environment
- **docker-compose.yml**: Docker Compose configuration for development
  - Spring Boot application container
  - Prometheus for metrics collection
  - Grafana for metrics visualization

#### 3.2 Production Environment
- **docker-compose-prod.yml**: Docker Compose configuration for production
  - Spring Boot application container with resource limits
  - Nginx as a reverse proxy for HTTP/HTTPS
  - Logging configuration
  - Security hardening

#### 3.3 Container Configuration
- **Dockerfile**: Multi-stage build process
  - Build stage: Maven build
  - Run stage: JRE runtime
  - Security: Non-root user, health checks

## Data Flow

1. **Authentication Flow**:
   - User accesses the application
   - Application redirects to Spotify authorization page
   - User authorizes the application
   - Spotify redirects back to the application with an authorization code
   - Application exchanges the code for an access token
   - Access token is stored for subsequent API calls

2. **User Data Flow**:
   - React frontend requests user profile from backend API
   - Backend uses stored access token to request data from Spotify API
   - Spotify returns user data
   - Backend transforms and forwards data to frontend
   - Frontend renders the user profile

## Security Considerations

- OAuth 2.0 for secure authentication with Spotify
- CSRF protection using state parameter in OAuth flow
- Docker security best practices:
  - Non-root user in containers
  - Resource limits
  - No privilege escalation
- Nginx reverse proxy in production for SSL termination

## Monitoring and Observability

- Spring Boot Actuator for application health and metrics
- Prometheus for metrics collection
- Grafana for metrics visualization and dashboards
- Docker health checks for container monitoring

## Conclusion

The Spotify User Profile Viewer is a well-structured application following modern best practices:
- Clear separation of concerns with layered architecture
- Secure authentication using OAuth 2.0
- Containerized deployment for consistency across environments
- Monitoring and observability built-in
- Production-ready configuration with security hardening

The application demonstrates a typical Spring Boot backend with React frontend architecture, with proper integration with third-party APIs (Spotify) and modern deployment practices using Docker.