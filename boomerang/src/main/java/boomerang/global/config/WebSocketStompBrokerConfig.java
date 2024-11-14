package boomerang.global.config;

import boomerang.global.interceptor.JwtHandshakeInterceptor;
import boomerang.global.utils.JwtUtil;
import boomerang.notifications.handler.WebSocketHandShakeHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    public WebSocketStompBrokerConfig(@Lazy JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/v1/ws/notifications")
                .setAllowedOrigins("http://localhost:8080", "http://localhost:5173", "http://54.252.224.76:80")
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil))
                .setHandshakeHandler(new WebSocketHandShakeHandler())
                .withSockJS();

        registry.addEndpoint("/ws/chat/{roomId}")
                .setAllowedOrigins("http://localhost:8080", "http://localhost:5173", "http://54.252.224.76:80")
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil))
                .setHandshakeHandler(new WebSocketHandShakeHandler())
                .withSockJS();
    }

}