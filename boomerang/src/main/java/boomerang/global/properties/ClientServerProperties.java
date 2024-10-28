package boomerang.global.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Component
@ConfigurationProperties(prefix = "client.server")
public class ClientServerProperties {
    @Value("${client.server.home}")
    private String home;

    @Value("${client.server.welcome}")
    private String welcome;
}