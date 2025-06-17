package toy.jim.personal.spotify.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import toy.jim.personal.spotify.model.SpotifyShowsResponse;
import toy.jim.personal.spotify.model.SpotifyUser;
import toy.jim.personal.spotify.service.UserServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServer userServer;

    @InjectMocks
    private UserController userController;

    private SpotifyUser mockUser;
    private SpotifyShowsResponse mockShowsResponse;

    @BeforeEach
    void setUp() {
        // Set up mock SpotifyUser
        SpotifyUser.ExternalUrls externalUrls = new SpotifyUser.ExternalUrls("https://open.spotify.com/user/123");
        SpotifyUser.Followers followers = new SpotifyUser.Followers(null, 100);
        SpotifyUser.Image image = new SpotifyUser.Image("https://example.com/image.jpg", 300, 300);
        SpotifyUser.ExplicitContent explicitContent = new SpotifyUser.ExplicitContent(true, false);
        
        mockUser = new SpotifyUser(
            "Test User",
            externalUrls,
            followers,
            "https://api.spotify.com/v1/users/123",
            "123",
            List.of(image),
            "user",
            "spotify:user:123",
            "US",
            "test@example.com",
            explicitContent,
            "premium"
        );

        // Set up mock SpotifyShowsResponse
        SpotifyShowsResponse.ExternalUrls showExternalUrls = new SpotifyShowsResponse.ExternalUrls("https://open.spotify.com/show/123");
        SpotifyShowsResponse.Followers showFollowers = new SpotifyShowsResponse.Followers(null, 100);
        SpotifyShowsResponse.Image showImage = new SpotifyShowsResponse.Image("https://example.com/image.jpg", 300, 300);
        
        SpotifyShowsResponse.Item item = new SpotifyShowsResponse.Item(
            showExternalUrls,
            showFollowers,
            List.of("podcast", "technology"),
            "https://api.spotify.com/v1/shows/123",
            "123",
            List.of(showImage),
            "Test Show",
            80,
            "show",
            "spotify:show:123"
        );
        
        mockShowsResponse = new SpotifyShowsResponse(
            "https://api.spotify.com/v1/shows?offset=0&limit=20",
            20,
            "https://api.spotify.com/v1/shows?offset=20&limit=20",
            0,
            null,
            100,
            List.of(item)
        );
    }

    @Test
    void testGetUserProfile_Success() {
        // Mock UserServer behavior
        when(userServer.getUser()).thenReturn(mockUser);

        // Test the method
        ResponseEntity<SpotifyUser> response = userController.getUserProfile();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test User", response.getBody().display_name());
        assertEquals("123", response.getBody().id());
        assertEquals("test@example.com", response.getBody().email());
        verify(userServer).getUser();
    }

    @Test
    void testGetUserProfile_NotFound() {
        // Mock UserServer behavior
        when(userServer.getUser()).thenReturn(null);

        // Test the method
        ResponseEntity<SpotifyUser> response = userController.getUserProfile();

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userServer).getUser();
    }

    @Test
    void testGetUserTracks_Success() {
        // Mock UserServer behavior
        when(userServer.getTracks()).thenReturn(mockShowsResponse);

        // Test the method
        ResponseEntity<SpotifyShowsResponse> response = userController.getUserTracks();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(20, response.getBody().limit());
        assertEquals(100, response.getBody().total());
        assertEquals(1, response.getBody().items().size());
        assertEquals("Test Show", response.getBody().items().get(0).name());
        verify(userServer).getTracks();
    }

    @Test
    void testGetUserTracks_NotFound() {
        // Mock UserServer behavior
        when(userServer.getTracks()).thenReturn(null);

        // Test the method
        ResponseEntity<SpotifyShowsResponse> response = userController.getUserTracks();

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userServer).getTracks();
    }
}