package boomerang.member.dto;

import boomerang.member.domain.Email;
import lombok.Getter;

@Getter
public class MemberCreateResponseDto {
    private Email email;
    private String nickname;


    public MemberCreateResponseDto() {}

    // 생성자
    public MemberCreateResponseDto(Email email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
