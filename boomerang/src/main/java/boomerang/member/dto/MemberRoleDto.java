package boomerang.member.dto;


import boomerang.member.domain.MemberRole;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberRoleDto {
    private MemberRole memberRole;

    public MemberRoleDto(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
