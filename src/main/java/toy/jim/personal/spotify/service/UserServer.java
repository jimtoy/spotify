package toy.jim.personal.spotify.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toy.jim.personal.spotify.config.SpotifyConfig;
import toy.jim.personal.spotify.model.SpotifyShowsResponse;
import toy.jim.personal.spotify.model.SpotifyUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServer {

    private final OAuthService oAuthService;
    private final SpotifyConfig spotifyConfig;

    /**
     * Get a Spotify user profile.
     *
     * @return The Spotify user profile as a SpotifyUser object
     */
    public SpotifyUser getUser() {
        return oAuthService.getSpotifyUser("https://api.spotify.com/v1/me");
    }

    public SpotifyShowsResponse getTracks(){
        return oAuthService.getSpotifyTracks("https://api.spotify.com/v1/me/top/artists");
    }

}
