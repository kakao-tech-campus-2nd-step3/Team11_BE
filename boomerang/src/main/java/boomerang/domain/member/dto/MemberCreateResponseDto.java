package boomerang.domain.member.dto;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Password;
import lombok.Getter;

@Getter
public class MemberCreateResponseDto {
    private Password password;
    private Email email;


    public MemberCreateResponseDto() {}

    // 생성자
    public MemberCreateResponseDto(
        Password password, Email email) {
        this.password = password;
        this.email = email;

    }
}
