package boomerang.global.utils;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import jakarta.servlet.http.Cookie;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CookieUtil {

    public static String Authorization = "Authorization";
    public static String Nickname = "Nickname";

    public static Cookie createAuthorizationCookies(String value) {

        Cookie cookie = new Cookie(Authorization, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 60);

        return cookie;
    }

    public static Cookie createNicknameCookies(String value) {
        try {
            // 공백과 특수 문자를 인코딩
            String encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie(Nickname, encodedValue);
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            cookie.setMaxAge(60 * 60 * 60);

            return cookie;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.COOKIES_ERROR);
        }
    }

}
