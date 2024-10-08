package boomerang.global.utils;

import jakarta.servlet.http.Cookie;

public class CookieUtil {

    public static String Authorization = "Authorization";

    public static Cookie createCookies(String value) {

        Cookie cookie = new Cookie(Authorization, value);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 60);

        return cookie;
    }

}
