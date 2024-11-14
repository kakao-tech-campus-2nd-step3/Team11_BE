package boomerang.notifications.handler;

import boomerang.notifications.dto.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@Component
public class WebSocketHandShakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String email = (String) attributes.get("user");

        if (email.isEmpty()) {
            return super.determineUser(request, wsHandler, attributes);
        }

        return new UserPrincipal(email);
    }
}
