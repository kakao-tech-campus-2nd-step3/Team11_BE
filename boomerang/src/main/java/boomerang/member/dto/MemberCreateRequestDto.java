package boomerang.member.dto;

import boomerang.member.domain.Email;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberCreateRequestDto {
    private Email email;
    private String nickname;



    // 생성자
    public MemberCreateRequestDto(Email email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    // MemberCreateServiceDto로 변환하는 메서드
    public MemberServiceDto toMemberCreateServiceDto() {
        return new MemberServiceDto(email, nickname);
    }

    public MemberServiceDto toMemberCreateServiceDto(Long id) {
        return new MemberServiceDto(email, nickname);
    }
}
