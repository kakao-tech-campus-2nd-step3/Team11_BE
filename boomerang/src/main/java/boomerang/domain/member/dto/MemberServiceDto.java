package boomerang.domain.member.dto;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Member;
import boomerang.domain.member.domain.MemberType;
import boomerang.domain.member.domain.ProfileImage;
import boomerang.domain.member.domain.ProgressStep;
import boomerang.domain.member.domain.ReturnDeposit;
import boomerang.domain.member.domain.SafetyScore;
import lombok.Getter;

@Getter
public class MemberServiceDto {
    private Long id;
    private Email email;
    private MemberType memberType;
    private ReturnDeposit returnDeposit;
    private SafetyScore safetyScore;
    private ProfileImage profileImage;
    private ProgressStep progressStep;

    protected MemberServiceDto() {}

    // 생성자
    public MemberServiceDto( Email email) {
        this.email = email;

    }

    // MemberCreateServiceDto로 변환하는 메서드
    public Member toMemberDomain() {
        return new Member(email);
    }
}
