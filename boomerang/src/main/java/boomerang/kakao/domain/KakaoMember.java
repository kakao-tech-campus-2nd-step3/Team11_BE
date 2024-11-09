package boomerang.kakao.domain;


public record KakaoMember(KakaoProfile kakaoProfile) {

    public String nickname() {
        return kakaoProfile.nickname();
    }

    public String email() {
        return kakaoProfile.email();
    }

    public String profileImage() {
        return kakaoProfile.profileImage();
    }

    public Long id() {
        return kakaoProfile.id();
    }

}
