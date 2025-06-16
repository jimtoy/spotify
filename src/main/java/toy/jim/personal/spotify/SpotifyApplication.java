package toy.jim.personal.spotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import toy.jim.personal.spotify.config.AuthConfig;

@SpringBootApplication
@EnableConfigurationProperties(AuthConfig.class)
public class SpotifyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotifyApplication.class, args);
    }

}
