package toy.jim.personal.spotify.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toy.jim.personal.spotify.config.AuthConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

/**
 * Simplified test class for OAuthService.
 * Complex tests involving HTTP mocking have been moved to OAuthServiceSimpleTest.
 */
@ExtendWith(MockitoExtension.class)
class OAuthServiceTest {

    @Mock
    private AuthConfig authConfig;

    @InjectMocks
    private OAuthService oAuthService;

    @Test
    void testGetBearerToken() {
        // Initially, bearer token should be null
        String bearerToken = oAuthService.getBearerToken();
        assertNull(bearerToken);
    }
}
