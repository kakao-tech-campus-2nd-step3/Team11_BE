package boomerang.domain.member.dto;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Password;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberCreateRequestDto {
    private Password password;
    private Email email;



    // 생성자
    public MemberCreateRequestDto(Password password, Email email) {
        this.password = password;
        this.email = email;
    }

    // MemberCreateServiceDto로 변환하는 메서드
    public MemberServiceDto toMemberCreateServiceDto() {
        return new MemberServiceDto(password, email);
    }

    public MemberServiceDto toMemberCreateServiceDto(Long id) {
        return new MemberServiceDto(password, email);
    }
}
