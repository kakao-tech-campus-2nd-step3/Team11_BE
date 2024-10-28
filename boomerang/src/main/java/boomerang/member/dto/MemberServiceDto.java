package boomerang.member.dto;

import boomerang.member.domain.*;
import lombok.Getter;

@Getter
public class MemberServiceDto {
    private Long id;
    private String email;
    private String nickname;
    private MemberType memberType;
    private ReturnDeposit returnDeposit;
    private SafetyScore safetyScore;
    private String profileImage;
    private ProgressStep progressStep;

    protected MemberServiceDto() {
    }

    // 생성자
    public MemberServiceDto(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;

    }

    // MemberCreateServiceDto로 변환하는 메서드
    public Member toMemberDomain() {
        return new Member(email, nickname);
    }
}
