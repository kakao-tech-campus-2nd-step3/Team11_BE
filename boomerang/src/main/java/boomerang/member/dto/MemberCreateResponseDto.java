package boomerang.member.dto;

import lombok.Getter;

@Getter
public class MemberCreateResponseDto {
    private String email;
    private String nickname;


    public MemberCreateResponseDto() {}

    // 생성자
    public MemberCreateResponseDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
