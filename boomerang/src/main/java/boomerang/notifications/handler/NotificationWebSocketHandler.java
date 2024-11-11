package boomerang.notifications.handler;

import boomerang.global.exception.BusinessException;
import boomerang.global.exception.WebNotificationException;
import boomerang.global.response.ErrorCode;
import boomerang.global.utils.JwtUtil;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.notifications.domain.Notification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    //현재 로그인 중인 개별 유저
    Map<Long, WebSocketSession> members;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public NotificationWebSocketHandler(JwtUtil jwtUtil, MemberService memberService) {
        this.members = new ConcurrentHashMap<>();
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    public boolean isMemberConnected(Member member) {
        return members.containsKey(member.getId()) && members.get(member.getId()).isOpen();
    }

    // 세션 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        session.getAttributes().put("authenticated", false);
    }

    // 특정 사용자에게 실시간 메시지 전송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            JsonNode jsonNode = mapper.readTree(payload);

            if (jsonNode.has("token")) {
                String token = jsonNode.get("token").asText();

                if (token == null || token.isEmpty() || token.split("\\.").length != 3) {
                    session.close(CloseStatus.NOT_ACCEPTABLE);
                    throw new WebNotificationException("유효하지 않은 토큰 형식입니다.");
                }

                //토큰 유효성 검증
                if (jwtUtil.isTokenExpired(token)) {
                    session.close(CloseStatus.NOT_ACCEPTABLE);
                    return;
                }
                String email = jwtUtil.getEmail(token);
                Member member = memberService.getMemberByEmail(email);
                members.put(member.getId(), session);
                session.getAttributes().put("authenticated", true);
                session.getAttributes().put("memberId", member.getId()); // 세션에 memberId 저장
            } else {
                session.close(CloseStatus.NOT_ACCEPTABLE);
            }

        } catch (JsonProcessingException e) {
            throw new WebNotificationException(e.getMessage() + " 웹 알림 전송 메시지 파싱 중 에러 발생");
        } catch (IOException e) {
            throw new WebNotificationException("웹 알림 전송 메시지 파싱 중 에러 발생: " + e.getMessage());
        }

    }

    //세션 끊김
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long memberId = (Long) session.getAttributes().get("memberId");
        System.out.println("(1)memberId = " + memberId);
        if (memberId != null) {
            members.remove(memberId);
        }
    }

    public void sendMessageToUser(Notification notification) {
        System.out.println("notification = " + notification);
        WebSocketSession userSession = members.get(notification.getMember().getId());
        System.out.println("userSession = " + userSession);

        if (userSession == null || !userSession.isOpen()) {
            throw new BusinessException(ErrorCode.NOTIFICATION_USER_SESSION_NOT_CONNECTION);
        }
        TextMessage textMessage = new TextMessage(notification.toString());
        try {
            userSession.sendMessage(textMessage);
        } catch (Exception e) {
            throw new WebNotificationException("웹 알림 전송 중 에러 발생: " + e.getMessage());
        }
    }

    public void sendMessageToUser(Long targetMemberId, String messageContent) {
        WebSocketSession userSession = members.get(targetMemberId);
        if (userSession == null || !userSession.isOpen()) {
            throw new BusinessException(ErrorCode.NOTIFICATION_USER_SESSION_NOT_CONNECTION);
        }
        TextMessage textMessage = new TextMessage(messageContent);
        try {
            userSession.sendMessage(textMessage);
        } catch (Exception e) {
            throw new WebNotificationException("웹 알림 전송 중 에러 발생: " + e.getMessage());
        }
    }

}
