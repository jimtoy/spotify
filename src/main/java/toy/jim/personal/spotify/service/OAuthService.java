package toy.jim.personal.spotify.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import toy.jim.personal.spotify.config.AuthConfig;
import toy.jim.personal.spotify.model.SpotifyShowsResponse;
import toy.jim.personal.spotify.model.SpotifyUser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Service for handling OAuth authentication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final AuthConfig authConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClients.createDefault();

    // Store state parameter to validate callback requests
    private String stateParam;

    // Store the bearer token for authenticated requests
    private String bearerToken;

    /**
     * Get an OAuth access token using client credentials flow.
     *
     * @return The access token or null if the request fails
     */
    public String getAccessToken() {
        HttpPost request = new HttpPost(authConfig.getTokenUrl());

        // Set OAuth-specific headers
        String auth = authConfig.getClientId() + ":" + authConfig.getClientSecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        request.setHeader("Authorization", "Basic " + encodedAuth);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Set body
        StringEntity entity = new StringEntity("grant_type=client_credentials",
                ContentType.APPLICATION_FORM_URLENCODED);
        request.setEntity(entity);

        return executeRequest(request, response -> {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();
            }
            log.error("OAuth response does not contain access_token: {}", response);
            return null;
        });
    }

    /**
     * Make an authenticated request to the Spotify API and deserialize the response into a SpotifyUser object.
     *
     * @param url The URL to call
     * @return The response as a SpotifyUser object or null if the request fails
     */
    public SpotifyUser getSpotifyUser(String url) {
        if (bearerToken == null) {
            log.error("Bearer token is null. User must authorize the application first.");
            return null;
        }
        HttpGet request = createAuthenticatedRequest(new HttpGet(url), bearerToken);
        return executeRequest(request, response ->
                objectMapper.readValue(response, SpotifyUser.class));
    }

    /**
     * Generate the Spotify authorization URL for the authorization code flow.
     *
     * @return The authorization URL to redirect the user to
     */
    public String getAuthorizationUrl() {
        // Generate a random state parameter to prevent CSRF attacks
        stateParam = UUID.randomUUID().toString();

        // Build the authorization URL with required parameters
        return UriComponentsBuilder.fromUriString(authConfig.getAuthorizationUrl())
                .queryParam("client_id", authConfig.getClientId())
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", authConfig.getRedirectUri())
                .queryParam("scope", authConfig.getScopes())
                .queryParam("state", stateParam)
                .build()
                .toUriString();
    }

    /**
     * Validate the state parameter from the callback to prevent CSRF attacks.
     *
     * @param state The state parameter from the callback
     * @return True if the state parameter is valid, false otherwise
     */
    public boolean validateState(String state) {
        return state != null && state.equals(stateParam);
    }

    /**
     * Exchange an authorization code for an access token.
     * This method implements the authorization code flow.
     *
     * @param code The authorization code received from Spotify
     * @return The access token or null if the exchange fails
     */
    public String exchangeCodeForToken(String code) {
        HttpPost request = new HttpPost(authConfig.getTokenUrl());

        // Set OAuth-specific headers
        String auth = authConfig.getClientId() + ":" + authConfig.getClientSecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        request.setHeader("Authorization", "Basic " + encodedAuth);
        request.setHeader("Content-Type", "application/x-www-form-urlencoded");

        // Prepare the request body
        String requestBody;
        try {
            requestBody = "grant_type=authorization_code" +
                    "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8.toString()) +
                    "&redirect_uri=" + URLEncoder.encode(authConfig.getRedirectUri(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Error encoding request parameters: {}", e.getMessage());
            return null;
        }

        // Set body
        StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED);
        request.setEntity(entity);

        // Execute the request and store the token
        bearerToken = executeRequest(request, response -> {
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();
            }
            log.error("OAuth response does not contain access_token: {}", response);
            return null;
        });

        return bearerToken;
    }

    /**
     * Creates an authenticated request with Bearer token
     *
     * @param request     The HTTP request to configure
     * @param accessToken The OAuth access token
     * @return The configured HTTP request
     */
    private <T extends HttpRequestBase> T createAuthenticatedRequest(T request, String accessToken) {
        request.setHeader("Authorization", "Bearer " + accessToken);
        request.setHeader("Content-Type", "application/json");
        return request;
    }

    /**
     * Executes an HTTP request and processes the response using the provided response handler
     *
     * @param request         The HTTP request to execute
     * @param responseHandler The handler to process the response
     * @return The processed result or null if the request fails
     */
    private <T> T executeRequest(HttpUriRequest request, ResponseHandler<T> responseHandler) {
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                String responseString = EntityUtils.toString(responseEntity);
                return responseHandler.handleResponse(responseString);
            }
        } catch (IOException e) {
            log.error("Error executing request to {}: {}", request.getURI(), e.getMessage());
        }
        return null;
    }

    /**
     * Make an authenticated request to the Spotify API and deserialize the response into a SpotifyShowsResponse object.
     *
     * @param url The URL to call
     * @return The response as a SpotifyShowsResponse object or null if the request fails
     */
    public SpotifyShowsResponse getSpotifyTracks(String url) {
        if (bearerToken == null) {
            log.error("Bearer token is null. User must authorize the application first.");
            return null;
        }
        HttpGet request = createAuthenticatedRequest(new HttpGet(url), bearerToken);
        return executeRequest(request, response ->
                objectMapper.readValue(response, SpotifyShowsResponse.class));
    }

    /**
     * Get the current bearer token.
     *
     * @return The current bearer token or null if not set
     */
    public String getBearerToken() {
        return bearerToken;
    }

    /**
     * Functional interface for handling HTTP responses
     */
    @FunctionalInterface
    private interface ResponseHandler<T> {
        T handleResponse(String responseBody) throws IOException;
    }
}
