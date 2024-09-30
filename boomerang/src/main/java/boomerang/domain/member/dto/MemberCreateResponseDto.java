package boomerang.domain.member.dto;

import boomerang.domain.member.domain.Email;
import lombok.Getter;

@Getter
public class MemberCreateResponseDto {
    private Email email;


    public MemberCreateResponseDto() {}

    // 생성자
    public MemberCreateResponseDto(Email email) {
        this.email = email;

    }
}
