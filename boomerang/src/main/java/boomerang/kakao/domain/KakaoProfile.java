package boomerang.kakao.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoProfile(Long id, KakaoAccount kakaoAccount, Properties properties) {
    public String nickname(){
        return properties().nickname();
    }

    public String email(){
        return kakaoAccount.email();
    }

    record Properties(String nickname){}

    record KakaoAccount(String email){}
}
