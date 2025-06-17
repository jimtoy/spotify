package toy.jim.personal.spotify.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;
import toy.jim.personal.spotify.service.OAuthService;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuthControllerTest {

    @Mock
    private OAuthService oAuthService;

    @InjectMocks
    private OAuthController oAuthController;

    private String mockAccessToken;
    private String mockBearerToken;
    private String mockAuthorizationUrl;
    private String mockState;

    @BeforeEach
    void setUp() {
        mockAccessToken = "test-access-token";
        mockBearerToken = "test-bearer-token";
        mockAuthorizationUrl = "https://accounts.spotify.com/authorize?client_id=test-client-id&response_type=code&redirect_uri=http://localhost:8080/api/oauth/callback&scope=user-read-private%20user-read-email&state=test-state";
        mockState = UUID.randomUUID().toString();
    }

    @Test
    void testGetToken_Success() {
        // Mock OAuthService behavior
        when(oAuthService.getAccessToken()).thenReturn(mockAccessToken);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.getToken();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockAccessToken, response.getBody().get("access_token"));
        verify(oAuthService).getAccessToken();
    }

    @Test
    void testGetToken_Failure() {
        // Mock OAuthService behavior
        when(oAuthService.getAccessToken()).thenReturn(null);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.getToken();

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to obtain access token", response.getBody().get("error"));
        verify(oAuthService).getAccessToken();
    }

    @Test
    void testGetBearerToken_Success() {
        // Mock OAuthService behavior
        when(oAuthService.getBearerToken()).thenReturn(mockBearerToken);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.getBearerToken();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockBearerToken, response.getBody().get("bearer_token"));
        verify(oAuthService).getBearerToken();
    }

    @Test
    void testGetBearerToken_Failure() {
        // Mock OAuthService behavior
        when(oAuthService.getBearerToken()).thenReturn(null);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.getBearerToken();

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No bearer token available. User must authorize the application first.", response.getBody().get("error"));
        verify(oAuthService).getBearerToken();
    }

    @Test
    void testAuthorize() {
        // Mock OAuthService behavior
        when(oAuthService.getAuthorizationUrl()).thenReturn(mockAuthorizationUrl);

        // Test the method
        RedirectView redirectView = oAuthController.authorize();

        // Verify
        assertEquals(mockAuthorizationUrl, redirectView.getUrl());
        verify(oAuthService).getAuthorizationUrl();
    }

    @Test
    void testCallback_Success() {
        // Mock OAuthService behavior
        when(oAuthService.validateState(mockState)).thenReturn(true);
        when(oAuthService.exchangeCodeForToken("test-code")).thenReturn(mockAccessToken);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.callback("test-code", mockState, null);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockAccessToken, response.getBody().get("access_token"));
        verify(oAuthService).validateState(mockState);
        verify(oAuthService).exchangeCodeForToken("test-code");
    }

    @Test
    void testCallback_WithError() {
        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.callback(null, null, "access_denied");

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("access_denied", response.getBody().get("error"));
        verifyNoInteractions(oAuthService);
    }

    @Test
    void testCallback_InvalidState() {
        // Mock OAuthService behavior
        when(oAuthService.validateState("invalid-state")).thenReturn(false);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.callback("test-code", "invalid-state", null);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid state parameter", response.getBody().get("error"));
        verify(oAuthService).validateState("invalid-state");
        verify(oAuthService, never()).exchangeCodeForToken(anyString());
    }

    @Test
    void testCallback_NoCode() {
        // Mock OAuthService behavior
        when(oAuthService.validateState(mockState)).thenReturn(true);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.callback(null, mockState, null);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("No authorization code received", response.getBody().get("error"));
        verify(oAuthService).validateState(mockState);
        verify(oAuthService, never()).exchangeCodeForToken(anyString());
    }

    @Test
    void testCallback_TokenExchangeFailure() {
        // Mock OAuthService behavior
        when(oAuthService.validateState(mockState)).thenReturn(true);
        when(oAuthService.exchangeCodeForToken("test-code")).thenReturn(null);

        // Test the method
        ResponseEntity<Map<String, String>> response = oAuthController.callback("test-code", mockState, null);

        // Verify
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Failed to obtain access token", response.getBody().get("error"));
        verify(oAuthService).validateState(mockState);
        verify(oAuthService).exchangeCodeForToken("test-code");
    }
}