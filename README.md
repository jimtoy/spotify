# Spotify User Profile Viewer

A Spring Boot application with a React frontend that displays Spotify user profile information.

## Features

- OAuth authentication with Spotify
- Display user profile information including:
  - Profile picture
  - Display name
  - Spotify ID
  - Follower count
  - Account type
- Link to open the profile in Spotify

## Prerequisites

- Java 21
- Maven
- Spotify Developer Account (for API credentials)

## Configuration

Set the following environment variables:

```
OAUTH_CLIENT_ID=your_spotify_client_id
OAUTH_CLIENT_SECRET=your_spotify_client_secret
OAUTH_REDIRECT_URI=http://127.0.0.1:8080/api/oauth/callback
```

You can also configure these in the `application.properties` file.

## Running the Application

1. Clone the repository
2. Set the environment variables
3. Run the application:

```
mvn spring-boot:run
```

## Docker 

# Build the image
docker compose build

# Start the container
docker compose up -d

# View logs
docker compose logs -f

# Stop the container
docker compose down


## Useful Commands
# View container logs
docker compose logs -f

# Check container status
docker compose ps

# View container metrics
docker stats

# Enter container shell
docker compose exec spotify-oauth sh

# Check container health
docker inspect spotify-oauth | grep -A 10 "Health"

# Backup container data (if needed)
docker compose run --rm spotify-oauth tar czf /backup/backup.tar.gz /data


4. Open a browser and navigate to `http://localhost:8080`

## API Endpoints

### User Endpoints
- `GET /api/users/profile` - Get the Spotify user profile
- `GET /api/users/tracks` - Get the user's top tracks

### OAuth Endpoints
- `GET /api/oauth/authorize` - Initiates the Spotify authorization flow
- `GET /api/oauth/callback` - OAuth callback endpoint (used by Spotify)
- `GET /api/oauth/bearer-token` - Get the current bearer token
- `GET /api/oauth/token` - Get an access token using client credentials flow

## Technology Stack

- Backend: Spring Boot 3.5.0
- Frontend: React 18
- Styling: Bootstrap 5.3.0
- API: Spotify Web API

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.0/maven-plugin)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/3.5.0/reference/actuator/index.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.0/reference/web/servlet.html)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the
parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
