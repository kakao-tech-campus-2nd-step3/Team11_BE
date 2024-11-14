package boomerang.global.config;

import boomerang.chat.service.ChatRoomService;
import boomerang.global.handler.ChatWebSocketHandler;
import boomerang.global.handler.WebSocketHandshakeInterceptor;
import boomerang.global.utils.JwtUtil;
import boomerang.member.service.MemberService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    public WebSocketConfig(JwtUtil jwtUtil, MemberService memberService, ChatRoomService chatRoomService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(chatRoomService), "/ws/chat/*")
                .addInterceptors(new WebSocketHandshakeInterceptor(jwtUtil, memberService, chatRoomService));
    }
}
