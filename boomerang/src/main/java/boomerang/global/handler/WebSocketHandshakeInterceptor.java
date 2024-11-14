package boomerang.global.handler;

import boomerang.chat.service.ChatRoomService;
import boomerang.consultation.domain.Consultation;
import boomerang.consultation.service.ConsultationService;
import boomerang.global.utils.JwtUtil;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
    private final ConsultationService consultationService;

    public WebSocketHandshakeInterceptor(JwtUtil jwtUtil, MemberService memberService, ChatRoomService chatRoomService, ConsultationService consultationService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.chatRoomService = chatRoomService;
        this.consultationService = consultationService;
    }

    @Override
    public boolean beforeHandshake(
            org.springframework.http.server.ServerHttpRequest request,
            org.springframework.http.server.ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {

        // 토큰을 쿼리 파라미터에서 추출
        String token = getTokenFromRequest(request);

        // 토큰 유효성 검사
        if (token == null || jwtUtil.isTokenExpired(token)) {
            return false; // 유효하지 않으면 연결 거부
        }

        // 토큰에서 사용자 이메일 추출 및 Member 객체 조회
        String email = jwtUtil.getEmail(token);
        Member member = memberService.getMemberByEmail(email);

        if (member == null) {
            return false; // 유효한 사용자가 아니면 연결 거부
        }

        // URI에서 roomId를 추출
        Long roomId = getRoomIdFromRequest(request);

        // 진행중인 상담인지 체크
        Consultation consultation = consultationService.validateConsultationExists(roomId);
        consultation.validateOnGoing();

        // 채팅방 소유자 검증
        chatRoomService.validateChatRoomOwnership(roomId, member);

        // 검증된 사용자 정보를 WebSocket 세션 속성에 저장
        attributes.put("nickname", member.getNickname());
        return true; // 연결 허용
    }

    @Override
    public void afterHandshake(
            org.springframework.http.server.ServerHttpRequest request,
            org.springframework.http.server.ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Exception exception) {
        // After handshake logic (if needed)
    }

    private String getTokenFromRequest(org.springframework.http.server.ServerHttpRequest request) {
        String query = request.getURI().getQuery();
        return query != null && query.startsWith("token=") ? query.split("=")[1] : null;
    }

    // /ws/chat/1
    private Long getRoomIdFromRequest(org.springframework.http.server.ServerHttpRequest request) {
        String path = request.getURI().getPath();
        String roomIdStr = path.split("/")[3];
        return Long.parseLong(roomIdStr);
    }
}
