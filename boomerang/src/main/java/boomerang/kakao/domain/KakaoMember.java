package boomerang.kakao.domain;


import boomerang.member.domain.Email;
import boomerang.member.dto.MemberCreateRequestDto;


public record KakaoMember(KakaoProfile kakaoProfile, String password) {
    public String name() {
        return kakaoProfile.properties().nickname();
    }

    public String email() {
        return kakaoProfile.id().toString() + kakaoProfile.properties().nickname();
    }


    public Long id() { return kakaoProfile.id(); }

    public MemberCreateRequestDto toMember() {

        return new MemberCreateRequestDto(new Email(email()));
    }

}
