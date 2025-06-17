package toy.jim.personal.spotify.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import toy.jim.personal.spotify.config.SpotifyConfig;
import toy.jim.personal.spotify.model.SpotifyShowsResponse;
import toy.jim.personal.spotify.model.SpotifyUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServerTest {

    @Mock
    private OAuthService oAuthService;

    @Mock
    private SpotifyConfig spotifyConfig;

    @InjectMocks
    private UserServer userServer;

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
    void testGetUser() {
        // Mock OAuthService behavior
        when(oAuthService.getSpotifyUser("https://api.spotify.com/v1/me")).thenReturn(mockUser);

        // Test the method
        SpotifyUser result = userServer.getUser();

        // Verify
        assertNotNull(result);
        assertEquals("Test User", result.display_name());
        assertEquals("123", result.id());
        assertEquals("test@example.com", result.email());
        verify(oAuthService).getSpotifyUser("https://api.spotify.com/v1/me");
    }

    @Test
    void testGetTracks() {
        // Mock OAuthService behavior
        when(oAuthService.getSpotifyTracks("https://api.spotify.com/v1/me/top/artists")).thenReturn(mockShowsResponse);

        // Test the method
        SpotifyShowsResponse result = userServer.getTracks();

        // Verify
        assertNotNull(result);
        assertEquals(20, result.limit());
        assertEquals(100, result.total());
        assertEquals(1, result.items().size());
        assertEquals("Test Show", result.items().get(0).name());
        verify(oAuthService).getSpotifyTracks("https://api.spotify.com/v1/me/top/artists");
    }
}