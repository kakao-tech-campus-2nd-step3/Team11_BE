package boomerang.notifications.handler;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.member.domain.Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationWebSocketHandler extends TextWebSocketHandler {

    //현재 로그인 중인 개별 유저
    Map<String, WebSocketSession> users = new ConcurrentHashMap<String, WebSocketSession>();

    // 세션 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Member member = getMember(session);


    }
    // 특정 사용자에게 실시간 메시지 전송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    }


    //세션 끊김
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }

    private Member getMember(WebSocketSession session) {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication();
        return principalDetails.getMember();
    }
}
