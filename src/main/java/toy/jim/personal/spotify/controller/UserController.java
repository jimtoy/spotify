package toy.jim.personal.spotify.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.jim.personal.spotify.model.SpotifyUser;
import toy.jim.personal.spotify.service.UserServer;

/**
 * Controller for Spotify user operations.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServer userServer;

    /**
     * Get a Spotify user profile.
     *
     * @return The Spotify user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<SpotifyUser> getUserProfile() {
        SpotifyUser user = userServer.getUser();
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}