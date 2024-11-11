package boomerang.member.dto;

import boomerang.member.domain.Member;
import boomerang.member.domain.MemberRole;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberLoginDto {

    private MemberRole memberRole;
    private String nickname;

    public MemberLoginDto(Member member) {
        this.memberRole = member.getMemberRole();
        this.nickname = member.getNickname();
    }
}
