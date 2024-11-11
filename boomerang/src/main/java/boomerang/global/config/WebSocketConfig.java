package boomerang.global.config;

import boomerang.global.handler.ChatWebSocketHandler;
import boomerang.global.utils.JwtUtil;
import boomerang.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(), "/ws/chat/{roomId}").setAllowedOrigins("*");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // STOMP 엔드포인트를 등록하고 SockJS를 활성화
        registry.addEndpoint("/ws/notifications")
                .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }
}
