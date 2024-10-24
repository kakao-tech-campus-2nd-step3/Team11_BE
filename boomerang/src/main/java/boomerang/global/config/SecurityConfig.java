package boomerang.global.config;

import boomerang.global.handler.SecurityAuthenticationEntryPoint;
import boomerang.global.oauth.service.PrincipalService;
import boomerang.global.utils.JwtFilter;
import boomerang.global.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
//createdAt과 updatedAt 필드를 자동으로 관리하기 위해 추가하는 코드
@EnableJpaAuditing
@RequiredArgsConstructor
@EnableWebMvc
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final PrincipalService principalService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //cors 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));        //3000 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));                            //모든 HTTP 메서드 허용
                        configuration.setAllowCredentials(true);                                                    //쿠키 사용
                        configuration.setAllowedHeaders(Collections.singletonList("*"));                            //클라이언트는 모든 타입의 헤더를 사용
                        configuration.setMaxAge(3600L);                                                             //프리플라이트 요청 결과 한시간동안 유효

                        //클라이언트가 응답에서 Set-Cookie, Authorization의 헤더에 접근할 수 있도록 허용
                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // H2 콘솔을 사용하기 위해 프레임 옵션 비활성화
        http
                .headers((headersConfigurer) -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));


        //JWTFilter 추가 (이후 JWT 필터 구현 후 추가)
        http
                .addFilterBefore(new JwtFilter(jwtUtil, principalService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(handeler -> handeler.authenticationEntryPoint(new SecurityAuthenticationEntryPoint()));

        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET, "/api/v1/member").authenticated()
                .requestMatchers( "/api/v1/board/comments/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/v1/board/*/comments", "/api/v1/board/*/likes").authenticated() // POST 요청 추가
                .requestMatchers(HttpMethod.DELETE, "/api/v1/board/*/likes").authenticated() // DELETE 요청 추가
                .anyRequest().permitAll());


        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // H2 콘솔을 사용하기 위해 프레임 옵션 비활성화
        http
                .headers((headersConfigurer) -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));


        return http.build();
    }

}
