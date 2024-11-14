package boomerang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableWebSocket
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
