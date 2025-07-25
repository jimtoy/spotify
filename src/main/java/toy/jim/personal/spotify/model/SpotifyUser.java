package toy.jim.personal.spotify.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record representing a Spotify user profile.
 * Based on the JSON structure returned by the Spotify API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyUser(
    String display_name,
    ExternalUrls external_urls,
    Followers followers,
    String href,
    String id,
    List<Image> images,
    String type,
    String uri,
    String country,
    String email,
    ExplicitContent explicit_content,
    String product
) {
    /**
     * Record representing external URLs for a Spotify entity.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExternalUrls(
        String spotify
    ) {}

    /**
     * Record representing follower information for a Spotify entity.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Followers(
        String href,
        int total
    ) {}

    /**
     * Record representing an image in Spotify.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Image(
        String url,
        int height,
        int width
    ) {}

    /**
     * Record representing explicit content settings for a Spotify user.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ExplicitContent(
        boolean filter_enabled,
        boolean filter_locked
    ) {}
}
