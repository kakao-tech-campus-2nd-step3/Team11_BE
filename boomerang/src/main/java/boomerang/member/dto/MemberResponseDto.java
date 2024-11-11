package boomerang.member.dto;

import boomerang.member.domain.Member;
import boomerang.member.domain.MemberRole;
import boomerang.progress.domain.ProgressType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberResponseDto {

    private final Long memberId;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final MemberRole memberRole;
    private final boolean emailVerified;
    private final boolean insuranceStatus;
    private final Long returnDeposit;
    private final Integer safetyScore;
    private final ProgressType progressType;

    public MemberResponseDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage();
        this.memberRole = member.getMemberRole();
        this.emailVerified = member.isEmailVerified();
        this.insuranceStatus = member.isInsuranceStatus();
        this.returnDeposit =
            member.getReturnDeposit() != null ? member.getReturnDeposit().getValue() : 0L;
        this.safetyScore = member.getSafetyScore() != null ? member.getSafetyScore().getValue() : 0;
        this.progressType = member.getProgressType();
    }
}