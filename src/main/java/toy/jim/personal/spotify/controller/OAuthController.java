package toy.jim.personal.spotify.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import toy.jim.personal.spotify.service.OAuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for demonstrating OAuth functionality.
 */
@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * Get an OAuth access token using client credentials flow.
     * This is different from the authorization code flow and doesn't require user authorization.
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

    /**
     * Get the current bearer token obtained from the authorization code flow.
     * This token is stored after a successful authorization and callback.
     *
     * @return The bearer token or an error message
     */
    @GetMapping("/bearer-token")
    public ResponseEntity<Map<String, String>> getBearerToken() {
        String bearerToken = oAuthService.getBearerToken();
        Map<String, String> response = new HashMap<>();

        if (bearerToken != null) {
            response.put("bearer_token", bearerToken);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "No bearer token available. User must authorize the application first.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Initiates the Spotify authorization code flow.
     * Redirects the user to the Spotify authorization page.
     *
     * @return A redirect to the Spotify authorization page
     */
    @GetMapping("/authorize")
    public RedirectView authorize() {
        String authorizationUrl = oAuthService.getAuthorizationUrl();
        log.info("Redirecting to Spotify authorization URL: {}", authorizationUrl);
        return new RedirectView(authorizationUrl);
    }

    /**
     * Handles the callback from Spotify after authorization.
     * Exchanges the authorization code for an access token.
     *
     * @param code  The authorization code from Spotify
     * @param state The state parameter to validate the request
     * @param error Any error that occurred during authorization
     * @return A response with the access token or an error message
     */
    @GetMapping("/callback")
    public RedirectView callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error) {


        // Check for errors from Spotify
        if (error != null) {
            log.error("Error during Spotify authorization: {}", error);
            return new RedirectView("/?error=" + error);

        }

        // Validate state parameter to prevent CSRF attacks
        if (!oAuthService.validateState(state)) {
            log.error("Invalid state parameter in callback");
            return new RedirectView("/?error=invalid_state");

        }

        // Exchange the authorization code for an access token
        if (code != null) {
            log.info("Received authorization code from Spotify, exchanging for token");
            String accessToken = oAuthService.exchangeCodeForToken(code);

            if (accessToken != null) {
                log.info("Successfully obtained access token");
                return new RedirectView("/?success=true");

            } else {
                log.error("Failed to exchange authorization code for token");
                return new RedirectView("/?error=token_exchange_failed");

            }
        } else {
            log.error("No authorization code received from Spotify");
            return new RedirectView("/?error=no_code");

        }
    }
}
