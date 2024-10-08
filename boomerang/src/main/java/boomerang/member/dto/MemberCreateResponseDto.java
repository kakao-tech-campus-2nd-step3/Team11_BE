package boomerang.member.dto;

import boomerang.member.domain.Email;
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
