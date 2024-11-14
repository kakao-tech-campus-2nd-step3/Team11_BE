package boomerang.global.interceptor;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    public JwtHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {

            String token = servletRequest.getServletRequest().getParameter("token");

            if (token.isEmpty()) {
                return false;
            }

            String email = jwtUtil.getEmail(token);
            log.info("Email extracted from token: {}", email);

            if (email.isEmpty()) {
                throw new BusinessException(ErrorCode.NOTIFICATION_JWT_NOT_CONNECTION);
            }
            attributes.put("user", email);
            return true;
        }

        throw new BusinessException(ErrorCode.NOTIFICATION_JWT_NOT_CONNECTION);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        log.info("JwtHandshakeInterceptor의 afterHandshake");
        return;
    }
}