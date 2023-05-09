package ch.zuehlke.challenge.bot.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class ApplicationProperties {
    private String name;
    private String token;
    private boolean tournamentBot;
    private String backendRootUri;
    private String backendJoinUrl;
    private String backendPlayUrl;
    private String webSocketUri;
}
