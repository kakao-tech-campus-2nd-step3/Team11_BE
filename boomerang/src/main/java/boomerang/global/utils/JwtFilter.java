package boomerang.global.utils;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.oauth.service.PrincipalService;
import boomerang.global.properties.ClientServerProperties;
import boomerang.member.domain.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PrincipalService principalService;
    private final ClientServerProperties clientServerProperties;

    public JwtFilter(JwtUtil jwtUtil, PrincipalService principalService, ClientServerProperties clientServerProperties) {
        this.jwtUtil = jwtUtil;
        this.principalService = principalService;
        this.clientServerProperties = clientServerProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        //토큰 소멸 시간 검증
        if (jwtUtil.isTokenExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 email 획득
        String email = jwtUtil.getEmail(token);

        //정상적인 jwt토큰이지만 DB에 없는 경우
        if (!principalService.existUserByUsername(email)) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 'welcome' 페이지와 /api/v1/member/random-nickname요청은 필터링하지 않음
        if ("/welcome".equals(path) || ("/api/v1/member/random-nickname".equals(path))) {
            filterChain.doFilter(request, response);
            return;
        }

        //MemberDetails에 회원 정보 객체 담기
        PrincipalDetails memberDetail = (PrincipalDetails) principalService.loadUserByEmail(email);

        if (memberDetail.getMemberRole().equals(MemberRole.INCOMPLETE_USER)
                && !("/api/v1/member/nickname".equals(path) && "PUT".equalsIgnoreCase(method))) {
            response.sendRedirect(clientServerProperties.getWelcome());
            filterChain.doFilter(request, response);
        }

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}