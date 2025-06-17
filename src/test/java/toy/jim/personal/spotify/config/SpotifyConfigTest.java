package toy.jim.personal.spotify.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
    "spotify.user-url=https://api.spotify.com/v1/me"
})
class SpotifyConfigTest {

    @Autowired
    private SpotifyConfig spotifyConfig;

    @Test
    void testPropertiesBinding() {
        assertNotNull(spotifyConfig);
        assertEquals("https://api.spotify.com/v1/me", spotifyConfig.getUserUrl());
    }

    @Test
    void testSettersAndGetters() {
        SpotifyConfig config = new SpotifyConfig();
        
        config.setUserUrl("https://api.spotify.com/v1/users/123");
        
        assertEquals("https://api.spotify.com/v1/users/123", config.getUserUrl());
    }
}