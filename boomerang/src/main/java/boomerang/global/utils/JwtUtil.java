package boomerang.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    // application.properties에서 시크릿 키를 가져옵니다.
    @Value("${jwt.secret-key}")
    private String secretKey;;

    // Key 객체는 나중에 초기화하기 위해 final이 아닌 필드로 선언합니다.
    private Key key;

    // Bean이 초기화될 때 Key 객체를 초기화합니다.
    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Long memberId, String email) {
        return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 2400000L))
            .signWith(key)
            .compact();
    }

    public Claims extractClaims(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims;
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get("email", String.class);
    }

    public boolean isTokenValid(String token, Long memberId) {
        Claims claims = extractClaims(token);
        return claims.getSubject().equals(memberId.toString()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().before(new Date());
    }

}
