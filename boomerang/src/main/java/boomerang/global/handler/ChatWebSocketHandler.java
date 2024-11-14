package boomerang.global.handler;

import boomerang.chat.domain.ChatMessage;
import boomerang.chat.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatRoomService chatRoomService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Long, Set<WebSocketSession>> roomSessions = new HashMap<>();

    public ChatWebSocketHandler(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = getRoomIdFromSession(session);
        roomSessions.computeIfAbsent(roomId, k -> new HashSet<>()).add(session);
    }

    // 유저 검증은 Interceptor 에서 진행된다
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        Long roomId = getRoomIdFromSession(session);
        String messageContent = textMessage.getPayload();

        // session에서 nickname 가져오기
        String nickname = (String) session.getAttributes().get("nickname");

        // 현재 시간으로 ChatMessage 객체 생성
        ChatMessage chatMessage = new ChatMessage(roomId, nickname, messageContent, LocalDateTime.now());

        // ChatMessage 객체를 JSON으로 변환하여 브로드캐스트
        String broadcastMessage = objectMapper.writeValueAsString(chatMessage);

        // 동일한 방의 모든 세션에 메시지 브로드캐스트
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (WebSocketSession s : sessions) {
                s.sendMessage(new TextMessage(broadcastMessage));
            }
        }

        // 메시지를 비동기로 DB에 저장
        chatRoomService.saveChatMessage(chatMessage);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long roomId = getRoomIdFromSession(session);
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }
    }

    private Long getRoomIdFromSession(WebSocketSession session) {
        String path = session.getUri().getPath();
        return Long.parseLong(path.split("/")[3]); // 경로에서 roomId를 Long으로 변환하여 가져옴
    }
}
