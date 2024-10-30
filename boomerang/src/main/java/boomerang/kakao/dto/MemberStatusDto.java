package boomerang.kakao.dto;

import boomerang.member.domain.Member;
import boomerang.member.domain.MemberRole;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberStatusDto {

    private MemberRole memberRole;

    public MemberStatusDto(Member member) {
        this.memberRole = member.getMemberRole();
    }
}
