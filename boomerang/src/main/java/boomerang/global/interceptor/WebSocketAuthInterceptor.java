package boomerang.global.interceptor;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.oauth.service.PrincipalService;
import boomerang.global.response.ErrorCode;
import boomerang.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final PrincipalService principalService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        //처음으로 websocket 연결요청시에만 실행
        String token = accessor.getFirstNativeHeader("Authorization");

        if (token == null || token.isBlank()) {
            throw new BusinessException(ErrorCode.LOGIN_REQUIRED);
        }

        //토큰 소멸 시간 검증
        if (jwtUtil.isTokenExpired(token)) {
            throw new BusinessException(ErrorCode.LOGIN_EXPIRATION);
        }

        //토큰에서 email 획득
        String email = jwtUtil.getEmail(token);

        //정상적인 jwt토큰이지만 DB에 없는 경우
        if (!principalService.existUserByUsername(email)) {
            throw new BusinessException(ErrorCode.LOGIN_MEMBER_NON_EXISTENT);
        }

        //MemberDetails에 회원 정보 객체 담기
        PrincipalDetails memberDetail = (PrincipalDetails) principalService.loadUserByEmail(email);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return message;

    }
}