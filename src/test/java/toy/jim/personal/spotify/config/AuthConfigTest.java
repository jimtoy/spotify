package toy.jim.personal.spotify.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
    "oauth.client-id=test-client-id",
    "oauth.client-secret=test-client-secret",
    "oauth.token-url=https://accounts.spotify.com/api/token",
    "oauth.authorization-url=https://accounts.spotify.com/authorize",
    "oauth.redirect-uri=http://localhost:8080/api/oauth/callback",
    "oauth.scopes=user-read-private user-read-email"
})
class AuthConfigTest {

    @Autowired
    private AuthConfig authConfig;

    @Test
    void testPropertiesBinding() {
        assertNotNull(authConfig);
        assertEquals("test-client-id", authConfig.getClientId());
        assertEquals("test-client-secret", authConfig.getClientSecret());
        assertEquals("https://accounts.spotify.com/api/token", authConfig.getTokenUrl());
        assertEquals("https://accounts.spotify.com/authorize", authConfig.getAuthorizationUrl());
        assertEquals("http://localhost:8080/api/oauth/callback", authConfig.getRedirectUri());
        assertEquals("user-read-private user-read-email", authConfig.getScopes());
    }

    @Test
    void testSettersAndGetters() {
        AuthConfig config = new AuthConfig();
        
        config.setClientId("new-client-id");
        config.setClientSecret("new-client-secret");
        config.setTokenUrl("new-token-url");
        config.setAuthorizationUrl("new-authorization-url");
        config.setRedirectUri("new-redirect-uri");
        config.setScopes("new-scopes");
        
        assertEquals("new-client-id", config.getClientId());
        assertEquals("new-client-secret", config.getClientSecret());
        assertEquals("new-token-url", config.getTokenUrl());
        assertEquals("new-authorization-url", config.getAuthorizationUrl());
        assertEquals("new-redirect-uri", config.getRedirectUri());
        assertEquals("new-scopes", config.getScopes());
    }
}