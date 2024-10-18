package boomerang.global.utils;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.oauth.service.PrincipalService;
import boomerang.member.domain.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final PrincipalService principalService;

    public JwtFilter(JwtUtil jwtUtil, PrincipalService principalService) {
        this.jwtUtil = jwtUtil;
        this.principalService = principalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        for (Cookie cookie : cookies) {

            System.out.println(cookie.getName());
            if (cookie.getName().equals("Authorization")) {

                authorization = cookie.getValue();
            }
        }

        //Authorization 헤더 검증
        if (authorization == null) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

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

        //MemberDetails에 회원 정보 객체 담기
        PrincipalDetails memberDetail = (PrincipalDetails) principalService.loadUserByEmail(email);
        if (memberDetail.getMemberRole() == MemberRole.INCOMPLETE_USER) {
            response.sendRedirect("http://localhost:5173/welcome");
            filterChain.doFilter(request, response);
        }

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(memberDetail, null, memberDetail.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}