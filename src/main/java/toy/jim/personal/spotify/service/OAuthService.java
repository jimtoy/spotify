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
import toy.jim.personal.spotify.config.AuthConfig;
import toy.jim.personal.spotify.model.SpotifyUser;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


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
     * @param url         The URL to call
     * @return The response as a SpotifyUser object or null if the request fails
     */
    public SpotifyUser getSpotifyUser(String url) {
        HttpGet request = createAuthenticatedRequest(new HttpGet(url), getAccessToken());
        return executeRequest(request, response ->
                objectMapper.readValue(response, SpotifyUser.class));
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
     * Functional interface for handling HTTP responses
     */
    @FunctionalInterface
    private interface ResponseHandler<T> {
        T handleResponse(String responseBody) throws IOException;
    }
}