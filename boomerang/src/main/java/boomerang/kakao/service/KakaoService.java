package boomerang.kakao.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import boomerang.kakao.domain.KakaoMember;
import boomerang.kakao.domain.KakaoProfile;
import boomerang.kakao.dto.KakaoTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoService {

    private static final MediaType CONTENT_TYPE = new MediaType(APPLICATION_FORM_URLENCODED, UTF_8);
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    private final RestClient restClient;
    @Value("${client_id}")
    private String clientId;

    public KakaoTokenResponseDto getAccessTokenFromKakao(String code) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); //바디 객체를 만드는 부분 Map으로 만들면 되고 key 값은 카카오문서에서 요청하는 이름으로
        map.add("grant_type", "authorization_code");
        map.add("client_id", clientId);
        map.add("redirect_uri", "http://172.31.2.16:8080/api/v1/auth/login/callback");
        map.add("code", code);
        RestClient restClient = RestClient.create();

        return restClient.post().uri("https://kauth.kakao.com/oauth/token") //요청 보낼 uri 등록
            .contentType(CONTENT_TYPE) //보내는 바디의 타입을 지정하는 부분
            .body(map) //바디 객체를 넣는 방법
            .retrieve() //요청을 보내고 응답을 가져오는 메서드, 실질적은 요청은 여기에서 일어남
            .toEntity(KakaoTokenResponseDto.class) // 요청을 어떤 엔티티로 바꿀지 등록하는 부분
            .getBody();

    }

    public KakaoMember getKakaoProfile(KakaoTokenResponseDto tokenResponse) {
        KakaoProfile kakaoProfile = restClient.post().uri("https://kapi.kakao.com/v2/user/me") // 쿼리파라미터 없이 요청시 전체정보 받음
            .contentType(CONTENT_TYPE).header(AUTHORIZATION, BEARER + tokenResponse.accessToken)
            .retrieve().toEntity(KakaoProfile.class).getBody();

        KakaoMember kakaoMember = new KakaoMember(kakaoProfile);
        return kakaoMember;

    }

    public KakaoService() {
        restClient = RestClient.create();
    }
}
