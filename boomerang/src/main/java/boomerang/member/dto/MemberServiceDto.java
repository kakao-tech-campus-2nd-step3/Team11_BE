package boomerang.member.dto;

import boomerang.member.domain.Member;
import boomerang.member.domain.MemberType;
import boomerang.member.domain.ProgressStep;
import boomerang.member.domain.ReturnDeposit;
import boomerang.member.domain.SafetyScore;
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

    protected MemberServiceDto() {}

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
