import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

