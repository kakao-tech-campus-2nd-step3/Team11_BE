package boomerang.global.utils;

import jakarta.servlet.http.Cookie;

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

        Cookie cookie = new Cookie(Nickname, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setMaxAge(60 * 60 * 60);

        return cookie;
    }

}
