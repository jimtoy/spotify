package toy.jim.personal.spotify.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SpotifyShowsResponseTest {

    @Test
    void testSpotifyShowsResponseCreation() {
        // Create nested objects
        SpotifyShowsResponse.ExternalUrls externalUrls = new SpotifyShowsResponse.ExternalUrls("https://open.spotify.com/show/123");
        SpotifyShowsResponse.Followers followers = new SpotifyShowsResponse.Followers(null, 100);
        SpotifyShowsResponse.Image image = new SpotifyShowsResponse.Image("https://example.com/image.jpg", 300, 300);
        
        // Create Item
        SpotifyShowsResponse.Item item = new SpotifyShowsResponse.Item(
            externalUrls,
            followers,
            List.of("podcast", "technology"),
            "https://api.spotify.com/v1/shows/123",
            "123",
            List.of(image),
            "Test Show",
            80,
            "show",
            "spotify:show:123"
        );
        
        // Create SpotifyShowsResponse
        SpotifyShowsResponse response = new SpotifyShowsResponse(
            "https://api.spotify.com/v1/shows?offset=0&limit=20",
            20,
            "https://api.spotify.com/v1/shows?offset=20&limit=20",
            0,
            null,
            100,
            List.of(item)
        );
        
        // Verify all fields
        assertEquals("https://api.spotify.com/v1/shows?offset=0&limit=20", response.href());
        assertEquals(20, response.limit());
        assertEquals("https://api.spotify.com/v1/shows?offset=20&limit=20", response.next());
        assertEquals(0, response.offset());
        assertNull(response.previous());
        assertEquals(100, response.total());
        assertEquals(1, response.items().size());
        assertEquals(item, response.items().get(0));
    }
    
    @Test
    void testItemCreation() {
        SpotifyShowsResponse.ExternalUrls externalUrls = new SpotifyShowsResponse.ExternalUrls("https://open.spotify.com/show/123");
        SpotifyShowsResponse.Followers followers = new SpotifyShowsResponse.Followers(null, 100);
        SpotifyShowsResponse.Image image = new SpotifyShowsResponse.Image("https://example.com/image.jpg", 300, 300);
        
        SpotifyShowsResponse.Item item = new SpotifyShowsResponse.Item(
            externalUrls,
            followers,
            List.of("podcast", "technology"),
            "https://api.spotify.com/v1/shows/123",
            "123",
            List.of(image),
            "Test Show",
            80,
            "show",
            "spotify:show:123"
        );
        
        assertEquals(externalUrls, item.external_urls());
        assertEquals(followers, item.followers());
        assertEquals(2, item.genres().size());
        assertEquals("podcast", item.genres().get(0));
        assertEquals("technology", item.genres().get(1));
        assertEquals("https://api.spotify.com/v1/shows/123", item.href());
        assertEquals("123", item.id());
        assertEquals(1, item.images().size());
        assertEquals(image, item.images().get(0));
        assertEquals("Test Show", item.name());
        assertEquals(80, item.popularity());
        assertEquals("show", item.type());
        assertEquals("spotify:show:123", item.uri());
    }
    
    @Test
    void testExternalUrlsCreation() {
        SpotifyShowsResponse.ExternalUrls externalUrls = new SpotifyShowsResponse.ExternalUrls("https://open.spotify.com/show/123");
        assertEquals("https://open.spotify.com/show/123", externalUrls.spotify());
    }
    
    @Test
    void testFollowersCreation() {
        SpotifyShowsResponse.Followers followers = new SpotifyShowsResponse.Followers("https://api.spotify.com/v1/shows/123/followers", 100);
        assertEquals("https://api.spotify.com/v1/shows/123/followers", followers.href());
        assertEquals(100, followers.total());
    }
    
    @Test
    void testImageCreation() {
        SpotifyShowsResponse.Image image = new SpotifyShowsResponse.Image("https://example.com/image.jpg", 300, 300);
        assertEquals("https://example.com/image.jpg", image.url());
        assertEquals(300, image.height());
        assertEquals(300, image.width());
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Create two identical responses
        SpotifyShowsResponse.ExternalUrls externalUrls1 = new SpotifyShowsResponse.ExternalUrls("https://open.spotify.com/show/123");
        SpotifyShowsResponse.Followers followers1 = new SpotifyShowsResponse.Followers(null, 100);
        SpotifyShowsResponse.Image image1 = new SpotifyShowsResponse.Image("https://example.com/image.jpg", 300, 300);
        
        SpotifyShowsResponse.Item item1 = new SpotifyShowsResponse.Item(
            externalUrls1,
            followers1,
            List.of("podcast", "technology"),
            "https://api.spotify.com/v1/shows/123",
            "123",
            List.of(image1),
            "Test Show",
            80,
            "show",
            "spotify:show:123"
        );
        
        SpotifyShowsResponse response1 = new SpotifyShowsResponse(
            "https://api.spotify.com/v1/shows?offset=0&limit=20",
            20,
            "https://api.spotify.com/v1/shows?offset=20&limit=20",
            0,
            null,
            100,
            List.of(item1)
        );
        
        SpotifyShowsResponse.ExternalUrls externalUrls2 = new SpotifyShowsResponse.ExternalUrls("https://open.spotify.com/show/123");
        SpotifyShowsResponse.Followers followers2 = new SpotifyShowsResponse.Followers(null, 100);
        SpotifyShowsResponse.Image image2 = new SpotifyShowsResponse.Image("https://example.com/image.jpg", 300, 300);
        
        SpotifyShowsResponse.Item item2 = new SpotifyShowsResponse.Item(
            externalUrls2,
            followers2,
            List.of("podcast", "technology"),
            "https://api.spotify.com/v1/shows/123",
            "123",
            List.of(image2),
            "Test Show",
            80,
            "show",
            "spotify:show:123"
        );
        
        SpotifyShowsResponse response2 = new SpotifyShowsResponse(
            "https://api.spotify.com/v1/shows?offset=0&limit=20",
            20,
            "https://api.spotify.com/v1/shows?offset=20&limit=20",
            0,
            null,
            100,
            List.of(item2)
        );
        
        // Test equals and hashCode
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}