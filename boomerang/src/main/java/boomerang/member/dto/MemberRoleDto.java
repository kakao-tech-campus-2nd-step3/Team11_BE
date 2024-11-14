package boomerang.member.dto;


import boomerang.member.domain.MemberRole;
import lombok.Getter;

@Getter
public class MemberRoleDto {
    private MemberRole memberRole;

    public MemberRoleDto(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
