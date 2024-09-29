package boomerang.domain.kakao.domain;


import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Password;
import boomerang.domain.member.dto.MemberCreateRequestDto;


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

    public Long id() { return kakaoProfile.id(); }

    public MemberCreateRequestDto toMember() {

        return new MemberCreateRequestDto(new Password(password()), new Email(email()));
    }

}
