package boomerang.global.utils;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookieUtil {

    public static String Authorization = "Authorization";
    public static String Nickname = "Nickname";
    @Value("${app.server.ip}")
    private static String serverIp;

    public static ResponseCookie createAuthorizationCookie(String value) {
        return ResponseCookie.from(Authorization, value)
                .path("/")
                .httpOnly(false)
                .secure(true)
                .sameSite("None")
                .domain(serverIp)
                .maxAge(60 * 60 * 60) // 쿠키 수명 설정
                .build();

    }

    public static ResponseCookie createNicknameCookies(String value) {
        try {
            // 공백과 특수 문자를 인코딩
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);

            return ResponseCookie.from(Nickname, encodedValue)
                    .path("/")
                    .httpOnly(false)    // HTTP 전용 아님
                    .secure(true)      // HTTPS 전송을 위한 설정
                    .sameSite("None")   // 크로스 도메인 요청에서도 쿠키 전송 가능
                    .domain(serverIp)
                    .maxAge(60 * 60 * 60)    // 쿠키 수명 설정
                    .build();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.COOKIES_ERROR);
        }
    }

}
