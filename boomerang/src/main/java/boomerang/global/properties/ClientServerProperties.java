package boomerang.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "client.server")
public class ClientServerProperties {
    private String home;
    private String welcome;
}