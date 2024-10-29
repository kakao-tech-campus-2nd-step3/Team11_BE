package boomerang.kakao.domain;


import boomerang.member.dto.MemberCreateRequestDto;


public record KakaoMember(KakaoProfile kakaoProfile) {
    public String nickname() {
        return kakaoProfile.nickname();
    }

    public String email() {
        return kakaoProfile.email();
    }


    public Long id() { return kakaoProfile.id(); }

    public MemberCreateRequestDto toMember() {
        return new MemberCreateRequestDto(this.email(),this.nickname());
    }

}
