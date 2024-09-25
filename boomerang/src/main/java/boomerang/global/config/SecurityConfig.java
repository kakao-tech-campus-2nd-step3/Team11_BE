package boomerang.global.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


//    private final CustomOAuth2UserService customOAuth2UserService;
//        DefaultOAuth2UserService 구현체
//        -  OAuth2 로그인 성공 후 실행되는 loadUser메서드를 가짐

//    private final CustomSuccessHandler customSuccessHandler;
//        SimpleUrlAuthenticationSuccessHandler 구현체
//        - 인증이 성공적으로 완료된 후 수행되는 onAuthenticationSuccess메서드를 가짐

//    private final JWTUtil jwtUtil;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //cors 설정
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));        //3000 허용
                    configuration.setAllowedMethods(Collections.singletonList("*"));                            //모든 HTTP 메서드 허용
                    configuration.setAllowCredentials(true);                                                    //쿠키 사용
                    configuration.setAllowedHeaders(Collections.singletonList("*"));                            //클라이언트는 모든 타입의 헤더를 사용
                    configuration.setMaxAge(3600L);                                                             //프리플라이트 요청 결과 한시간동안 유효

                    //클라이언트가 응답에서 Set-Cookie, Authorization의 헤더에 접근할 수 있도록 허용
                    configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));


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


        //JWTFilter 추가 (이후 JWT 필터 구현 후 추가)
//        http
//                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        //oauth2 (이후
//        http
//                .oauth2Login((oauth2) -> oauth2
//                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                                .userService(customOAuth2UserService))
//                        .successHandler(customSuccessHandler)
//                );

        //경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}