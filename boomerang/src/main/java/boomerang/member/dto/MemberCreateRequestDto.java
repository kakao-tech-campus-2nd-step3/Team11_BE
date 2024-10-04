package boomerang.member.dto;

import boomerang.member.domain.Email;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberCreateRequestDto {
    private Email email;



    // 생성자
    public MemberCreateRequestDto(Email email) {
        this.email = email;
    }

    // MemberCreateServiceDto로 변환하는 메서드
    public MemberServiceDto toMemberCreateServiceDto() {
        return new MemberServiceDto(email);
    }

    public MemberServiceDto toMemberCreateServiceDto(Long id) {
        return new MemberServiceDto(email);
    }
}
