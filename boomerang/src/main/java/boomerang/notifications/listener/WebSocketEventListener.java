package boomerang.notifications.listener;

import boomerang.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final NotificationService notificationService;
    private final ConcurrentHashMap<String, Boolean> userFirstConnectionMap = new ConcurrentHashMap<>();

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // 사용자 ID나 이메일을 키로 사용할 수 있도록 수정
        String memberEmail = headerAccessor.getUser() != null ? headerAccessor.getUser().getName() : sessionId;

        // 첫 연결인지 확인
        if (userFirstConnectionMap.putIfAbsent(memberEmail, true) == null) {
            log.info("첫 WebSocket 연결 발생 - 사용자 ID: {}", memberEmail);
            // 첫 연결 시에만 처리할 작업 추가
            notificationService.sendUnreadNotificationsOfMember(memberEmail);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = headerAccessor.getUser();

        if (user != null && user.getName() != null) {
            userFirstConnectionMap.remove(user.getName());
            log.info("User disconnected: {}", user.getName());
        }
    }
}