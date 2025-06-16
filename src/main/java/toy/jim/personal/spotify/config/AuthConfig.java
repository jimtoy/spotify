package toy.jim.personal.spotify.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OAuth properties.
 * This class binds properties from application.properties with the prefix "oauth"
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "oauth")
public class AuthConfig {
    
    /**
     * The client ID for OAuth authentication
     */
    private String clientId;
    
    /**
     * The client secret for OAuth authentication
     */
    private String clientSecret;
    
    /**
     * The token URL for OAuth authentication
     */
    private String tokenUrl;
}