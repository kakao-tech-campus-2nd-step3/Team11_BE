package boomerang.kakao.service;

import boomerang.kakao.domain.KakaoMember;
import boomerang.kakao.domain.KakaoProfile;
import boomerang.kakao.dto.KakaoTokenDto;
import boomerang.kakao.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class KakaoService {

    private static final MediaType CONTENT_TYPE = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final RestClient restClient;
    @Value("${client_id}")
    private String clientId;

    @Value("${app.server.ip}")
    private String serverIp;

    public KakaoService() {
        restClient = RestClient.create();
    }

    public KakaoTokenResponseDto getAccessTokenFromKakao(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("redirect_uri", String.format("http://%s:8080/api/v1/auth/login/callback", serverIp));
        map.add("code", code);

        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(CONTENT_TYPE)
                .body(map)
                .retrieve()
                .toEntity(KakaoTokenResponseDto.class)
                .getBody();
    }

    public KakaoMember getKakaoProfile(KakaoTokenResponseDto tokenResponse) {
        KakaoProfile kakaoProfile = restClient.post().uri("https://kapi.kakao.com/v2/user/me") // 쿼리파라미터 없이 요청시 전체정보 받음
                .contentType(CONTENT_TYPE).header(AUTHORIZATION, BEARER + tokenResponse.accessToken)
                .retrieve().toEntity(KakaoProfile.class).getBody();

        KakaoMember kakaoMember = new KakaoMember(kakaoProfile);
        return kakaoMember;

    }

    public KakaoMember getKakaoProfile(KakaoTokenDto kakaoTokenDto) {

        KakaoProfile kakaoProfile = restClient.post()
                .uri("https://kapi.kakao.com/v2/user/me") // 쿼리파라미터 없이 요청시 전체정보 받음
                .contentType(CONTENT_TYPE).header(AUTHORIZATION, BEARER + kakaoTokenDto.getAccessToken())
                .retrieve().
                toEntity(KakaoProfile.class).getBody();

        KakaoMember kakaoMember = new KakaoMember(kakaoProfile);
        return kakaoMember;

    }
}
