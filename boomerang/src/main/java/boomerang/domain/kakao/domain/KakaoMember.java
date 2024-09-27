package boomerang.domain.kakao.domain;


public record KakaoMember(KakaoProfile kakaoProfile, String password) {
    public String name() {
        return kakaoProfile.properties().nickname();
    }

    public String email() {
        return kakaoProfile.id().toString() + kakaoProfile.properties().nickname();
    }

    public String password() {
        return password;
    }


}
