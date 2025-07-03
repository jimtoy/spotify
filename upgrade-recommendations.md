# Library and JDK Upgrade Recommendations

After reviewing the codebase, here are my recommendations for library and JDK upgrades:

## Current Versions

- Java: 21
- Spring Boot: 3.5.0
- Apache HttpClient: 4.5.14
- JaCoCo: 0.8.11
- Maven Wrapper: 3.3.2
- Docker Compose: 3.8

## Recommended Upgrades

### 1. Spring Boot: 3.5.0 → 3.2.4 (Downgrade)

**Justification:**
- Spring Boot 3.5.0 is currently a milestone release (not a stable release)
- Spring Boot 3.2.4 is the latest stable release as of now
- Milestone releases should not be used in production environments
- This will provide better stability and support

### 2. Apache HttpClient: 4.5.14 → 5.3.1

**Justification:**
- Apache HttpClient 4.x is an older generation API
- HttpClient 5.x offers:
  - Improved performance
  - Better HTTP/2 support
  - Fluent API for easier request building
  - Enhanced concurrency and connection management
  - Better compliance with modern HTTP standards

**Implementation Notes:**
- This will require refactoring the OAuthService class to use the new API
- The changes would primarily affect the HTTP request creation and execution methods

### 3. Lombok: Latest Version

**Justification:**
- The pom.xml doesn't specify a version for Lombok, relying on the version managed by Spring Boot
- Ensuring the latest version provides access to newer features and bug fixes

### 4. Java: Consider JDK 22 LTS

**Justification:**
- Java 22 is the latest LTS release with long-term support
- Provides performance improvements and new language features
- The project's IDE configuration already references JDK 24, suggesting readiness for newer Java versions

**Implementation Notes:**
- Update java.version, maven.compiler.source, and maven.compiler.target in pom.xml
- Update Dockerfile to use eclipse-temurin:22-jre-alpine

### 5. Maven Wrapper: 3.3.2 → 3.9.6

**Justification:**
- 3.9.6 is the latest stable version
- Provides bug fixes and performance improvements

## Additional Recommendations

### 1. Spring Boot Maven Plugin Duplication

There's a duplication of the Spring Boot Maven Plugin in the pom.xml file. Remove one of the duplicate entries to avoid potential build issues.

### 2. Consider Using Spring WebClient Instead of HttpClient

**Justification:**
- WebClient is part of Spring WebFlux and provides a modern, functional API
- Better integration with Spring's ecosystem
- Support for reactive programming
- More concise and readable code

### 3. Add Dependency Versions Management

Consider adding explicit versions for all dependencies to ensure reproducible builds and easier version management.

## Compatibility Considerations

- The downgrade to Spring Boot 3.2.4 should be backward compatible with the current code
- Upgrading to HttpClient 5.x will require code changes but should not affect other components
- Java 22 is backward compatible with Java 21 code
- All recommended upgrades have been checked for compatibility with each other

## Implementation Priority

1. Fix the Spring Boot Maven Plugin duplication (critical, easy fix)
2. Downgrade Spring Boot to 3.2.4 (high priority for stability)
3. Upgrade Maven Wrapper (medium priority, easy fix)
4. Upgrade HttpClient to 5.x (medium priority, requires code changes)
5. Consider Java 22 upgrade (lower priority, requires environment changes)