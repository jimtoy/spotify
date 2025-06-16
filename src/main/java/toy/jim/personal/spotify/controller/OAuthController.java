package toy.jim.personal.spotify.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toy.jim.personal.spotify.service.OAuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for demonstrating OAuth functionality.
 */
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * Get an OAuth access token.
     *
     * @return The access token or an error message
     */
    @GetMapping("/token")
    public ResponseEntity<Map<String, String>> getToken() {
        String accessToken = oAuthService.getAccessToken();
        Map<String, String> response = new HashMap<>();
        
        if (accessToken != null) {
            response.put("access_token", accessToken);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Failed to obtain access token");
            return ResponseEntity.badRequest().body(response);
        }
    }
}