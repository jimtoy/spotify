# C3 Diagram for Spotify User Profile Viewer

## Level 1: Context Diagram

```plantuml
@startuml Context Diagram
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

Person(user, "User", "A user who wants to view their Spotify profile and top artists")
System(spotifyProfileViewer, "Spotify User Profile Viewer", "Allows users to view their Spotify profile information and top artists")
System_Ext(spotifyAPI, "Spotify API", "Provides authentication and user data")

Rel(user, spotifyProfileViewer, "Uses", "HTTPS")
Rel(spotifyProfileViewer, spotifyAPI, "Authenticates and fetches data from", "HTTPS/REST")
Rel(spotifyAPI, spotifyProfileViewer, "Returns user data and tokens to", "HTTPS/REST")

@enduml
```

## Level 2: Container Diagram

```plantuml
@startuml Container Diagram
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

Person(user, "User", "A user who wants to view their Spotify profile and top artists")
System_Boundary(spotifyProfileViewer, "Spotify User Profile Viewer") {
    Container(webApplication, "Spring Boot Application", "Java, Spring Boot 3.5.0", "Handles API requests, authentication, and business logic")
    Container(webBrowser, "Web Browser", "HTML, CSS, JavaScript", "Displays the user interface")
    Container(reactApp, "React Application", "React 18, Bootstrap 5.3.0", "Provides interactive UI components")
    ContainerDb(tokenStorage, "Token Storage", "In-memory", "Stores OAuth tokens temporarily")
}

System_Ext(spotifyAPI, "Spotify API", "Provides authentication and user data")
System_Ext(monitoring, "Monitoring", "Prometheus/Grafana", "Collects and visualizes metrics")

Rel(user, webBrowser, "Uses", "HTTPS")
Rel(webBrowser, reactApp, "Displays")
Rel(reactApp, webApplication, "Makes API calls to", "HTTPS/REST")
Rel(webApplication, tokenStorage, "Stores and retrieves tokens from")
Rel(webApplication, spotifyAPI, "Authenticates and fetches data from", "HTTPS/REST")
Rel(webApplication, monitoring, "Sends metrics to")

@enduml
```

## Level 3: Component Diagram

```plantuml
@startuml Component Diagram
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Component.puml

Container_Boundary(webApplication, "Spring Boot Application") {
    Component(oauthController, "OAuthController", "Spring REST Controller", "Handles OAuth authentication endpoints")
    Component(userController, "UserController", "Spring REST Controller", "Handles user data endpoints")
    Component(oauthService, "OAuthService", "Spring Service", "Manages OAuth authentication and token exchange")
    Component(userService, "UserServer", "Spring Service", "Fetches and processes user data")
    Component(authConfig, "AuthConfig", "Spring Configuration", "Configures OAuth properties")
    Component(spotifyConfig, "SpotifyConfig", "Spring Configuration", "Configures Spotify API properties")
    Component(spotifyUserModel, "SpotifyUser", "Java Record", "Data model for user profile")
    Component(spotifyShowsModel, "SpotifyShowsResponse", "Java Record", "Data model for top artists")
}

Container_Boundary(reactApplication, "React Application") {
    Component(app, "App", "React Component", "Main application component")
    Component(loginButton, "LoginButton", "React Component", "Handles authentication UI")
    Component(userProfile, "UserProfile", "React Component", "Displays user profile")
    Component(userTracks, "UserTracks", "React Component", "Displays top artists")
}

System_Ext(spotifyAPI, "Spotify API")
ContainerDb(tokenStorage, "Token Storage")

Rel(oauthController, oauthService, "Uses")
Rel(userController, userService, "Uses")
Rel(oauthService, authConfig, "Reads configuration from")
Rel(userService, spotifyConfig, "Reads configuration from")
Rel(oauthService, tokenStorage, "Stores and retrieves tokens")
Rel(userService, oauthService, "Uses for authenticated requests")
Rel(oauthService, spotifyAPI, "Authenticates with")
Rel(userService, spotifyAPI, "Fetches data from")
Rel(userService, spotifyUserModel, "Creates instances of")
Rel(userService, spotifyShowsModel, "Creates instances of")

Rel(app, loginButton, "Contains")
Rel(app, userProfile, "Contains")
Rel(app, userTracks, "Contains")
Rel(loginButton, oauthController, "Calls", "HTTPS/REST")
Rel(userProfile, userController, "Calls", "HTTPS/REST")
Rel(userTracks, userController, "Calls", "HTTPS/REST")

@enduml
```

## Deployment Diagram

```plantuml
@startuml Deployment Diagram
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Deployment.puml

Deployment_Node(devEnv, "Development Environment", "Developer Workstation/Local") {
    Container(devApp, "Spring Boot Application", "Java, Spring Boot 3.5.0", "Development instance")
}

Deployment_Node(prodEnv, "Production Environment", "Docker Containers") {
    Deployment_Node(dockerHost, "Docker Host", "Linux") {
        Container(springBootContainer, "Spring Boot Container", "Docker", "Runs the Spring Boot application")
        Container(nginxContainer, "Nginx Container", "Docker", "Reverse proxy for HTTP/HTTPS")
        Container(prometheusContainer, "Prometheus Container", "Docker", "Metrics collection")
        Container(grafanaContainer, "Grafana Container", "Docker", "Metrics visualization")
    }
}

Deployment_Node(browser, "User's Device", "Any") {
    Container(webBrowser, "Web Browser", "Any modern browser", "Displays the UI")
}

Deployment_Node(spotifyCloud, "Spotify Cloud", "Spotify's Infrastructure") {
    Container_Ext(spotifyAPI, "Spotify API", "REST API", "Provides authentication and data")
}

Rel(webBrowser, nginxContainer, "Accesses", "HTTPS")
Rel(nginxContainer, springBootContainer, "Routes requests to", "HTTP")
Rel(springBootContainer, spotifyAPI, "Makes API calls to", "HTTPS")
Rel(springBootContainer, prometheusContainer, "Sends metrics to")
Rel(prometheusContainer, grafanaContainer, "Provides data for")
Rel(devApp, spotifyAPI, "Makes API calls to", "HTTPS")

@enduml
```

## Notes

This C3 diagram follows the C4 model approach (Context, Container, Component) with an additional deployment view. The diagrams are written in PlantUML format with the C4-PlantUML extension, which provides standardized notation for architectural diagrams.

The diagrams show:

1. **Context Diagram (Level 1)**: The overall system, its users, and external dependencies
2. **Container Diagram (Level 2)**: The high-level technical components (containers) that make up the system
3. **Component Diagram (Level 3)**: The internal components of each container and their interactions
4. **Deployment Diagram**: How the system is deployed across different environments

These diagrams provide a comprehensive view of the Spotify User Profile Viewer's architecture at different levels of abstraction, making it easier to understand the system from both high-level and detailed perspectives.