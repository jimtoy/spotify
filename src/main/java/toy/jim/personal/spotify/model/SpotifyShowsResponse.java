package toy.jim.personal.spotify.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record representing a paginated response of shows from the Spotify API.
 * Based on the JSON structure returned by the Spotify API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SpotifyShowsResponse(
    String href,
    int limit,
    String next,
    int offset,
    String previous,
    int total,
    List<Item> items
) {
    /**
     * Record representing an item in the shows response.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Item(
        ExternalUrls external_urls,
        Followers followers,
        List<String> genres,
        String href,
        String id,
        List<Image> images,
        String name,
        int popularity,
        String type,
        String uri
    ) {}

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
}
