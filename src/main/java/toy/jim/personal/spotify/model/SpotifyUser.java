package toy.jim.personal.spotify.model;

import java.util.List;

/**
 * Record representing a Spotify user profile.
 * Based on the JSON structure returned by the Spotify API.
 */
public record SpotifyUser(
    String display_name,
    ExternalUrls external_urls,
    Followers followers,
    String href,
    String id,
    List<Image> images,
    String type,
    String uri
) {
    /**
     * Record representing external URLs for a Spotify entity.
     */
    public record ExternalUrls(
        String spotify
    ) {}

    /**
     * Record representing follower information for a Spotify entity.
     */
    public record Followers(
        String href,
        int total
    ) {}

    /**
     * Record representing an image in Spotify.
     */
    public record Image(
        String url,
        int height,
        int width
    ) {}
}