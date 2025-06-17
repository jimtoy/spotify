package toy.jim.personal.spotify.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SpotifyUserTest {

    @Test
    void testSpotifyUserCreation() {
        // Create nested objects
        SpotifyUser.ExternalUrls externalUrls = new SpotifyUser.ExternalUrls("https://open.spotify.com/user/123");
        SpotifyUser.Followers followers = new SpotifyUser.Followers(null, 100);
        SpotifyUser.Image image = new SpotifyUser.Image("https://example.com/image.jpg", 300, 300);
        SpotifyUser.ExplicitContent explicitContent = new SpotifyUser.ExplicitContent(true, false);
        
        // Create SpotifyUser
        SpotifyUser user = new SpotifyUser(
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
        
        // Verify all fields
        assertEquals("Test User", user.display_name());
        assertEquals(externalUrls, user.external_urls());
        assertEquals(followers, user.followers());
        assertEquals("https://api.spotify.com/v1/users/123", user.href());
        assertEquals("123", user.id());
        assertEquals(1, user.images().size());
        assertEquals(image, user.images().get(0));
        assertEquals("user", user.type());
        assertEquals("spotify:user:123", user.uri());
        assertEquals("US", user.country());
        assertEquals("test@example.com", user.email());
        assertEquals(explicitContent, user.explicit_content());
        assertEquals("premium", user.product());
    }
    
    @Test
    void testExternalUrlsCreation() {
        SpotifyUser.ExternalUrls externalUrls = new SpotifyUser.ExternalUrls("https://open.spotify.com/user/123");
        assertEquals("https://open.spotify.com/user/123", externalUrls.spotify());
    }
    
    @Test
    void testFollowersCreation() {
        SpotifyUser.Followers followers = new SpotifyUser.Followers("https://api.spotify.com/v1/users/123/followers", 100);
        assertEquals("https://api.spotify.com/v1/users/123/followers", followers.href());
        assertEquals(100, followers.total());
    }
    
    @Test
    void testImageCreation() {
        SpotifyUser.Image image = new SpotifyUser.Image("https://example.com/image.jpg", 300, 300);
        assertEquals("https://example.com/image.jpg", image.url());
        assertEquals(300, image.height());
        assertEquals(300, image.width());
    }
    
    @Test
    void testExplicitContentCreation() {
        SpotifyUser.ExplicitContent explicitContent = new SpotifyUser.ExplicitContent(true, false);
        assertTrue(explicitContent.filter_enabled());
        assertFalse(explicitContent.filter_locked());
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Create two identical users
        SpotifyUser.ExternalUrls externalUrls1 = new SpotifyUser.ExternalUrls("https://open.spotify.com/user/123");
        SpotifyUser.Followers followers1 = new SpotifyUser.Followers(null, 100);
        SpotifyUser.Image image1 = new SpotifyUser.Image("https://example.com/image.jpg", 300, 300);
        SpotifyUser.ExplicitContent explicitContent1 = new SpotifyUser.ExplicitContent(true, false);
        
        SpotifyUser user1 = new SpotifyUser(
            "Test User",
            externalUrls1,
            followers1,
            "https://api.spotify.com/v1/users/123",
            "123",
            List.of(image1),
            "user",
            "spotify:user:123",
            "US",
            "test@example.com",
            explicitContent1,
            "premium"
        );
        
        SpotifyUser.ExternalUrls externalUrls2 = new SpotifyUser.ExternalUrls("https://open.spotify.com/user/123");
        SpotifyUser.Followers followers2 = new SpotifyUser.Followers(null, 100);
        SpotifyUser.Image image2 = new SpotifyUser.Image("https://example.com/image.jpg", 300, 300);
        SpotifyUser.ExplicitContent explicitContent2 = new SpotifyUser.ExplicitContent(true, false);
        
        SpotifyUser user2 = new SpotifyUser(
            "Test User",
            externalUrls2,
            followers2,
            "https://api.spotify.com/v1/users/123",
            "123",
            List.of(image2),
            "user",
            "spotify:user:123",
            "US",
            "test@example.com",
            explicitContent2,
            "premium"
        );
        
        // Test equals and hashCode
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}