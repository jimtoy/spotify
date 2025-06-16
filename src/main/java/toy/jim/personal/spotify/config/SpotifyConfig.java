package toy.jim.personal.spotify.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spotify")
public class SpotifyConfig {

    private String userUrl;
}
