package boomerang.domain.member.dto;

import boomerang.domain.member.domain.Created_at;
import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Insurance_status;
import boomerang.domain.member.domain.Is_deleted;
import boomerang.domain.member.domain.Member_type;
import boomerang.domain.member.domain.Password;
import boomerang.domain.member.domain.Profile_image;
import boomerang.domain.member.domain.Progress_step;
import boomerang.domain.member.domain.Return_deposit;
import boomerang.domain.member.domain.Safety_score;
import boomerang.domain.member.domain.Updated_at;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberCreateRequestDto {
    private Password password;
    private Email email;
    private Member_type member_type;
    private Insurance_status insurance_status;
    private Return_deposit return_deposit;
    private Safety_score safety_score;
    private Profile_image profile_image;
    private Progress_step progress_step;
    private Created_at created_at;
    private Updated_at updated_at;
    private Is_deleted is_deleted;


    // 생성자
    public MemberCreateRequestDto(Password password, Email email, Member_type member_type,
        Insurance_status insurance_status, Return_deposit return_deposit, Safety_score safety_score, Profile_image profile_image,
        Progress_step progress_step, Created_at created_at, Updated_at updated_at, Is_deleted is_deleted) {
        this.password = password;
        this.email = email;
        this.member_type = member_type;
        this.insurance_status = insurance_status;
        this.return_deposit = return_deposit;
        this.safety_score = safety_score;
        this.profile_image = profile_image;
        this.progress_step = progress_step;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.is_deleted = is_deleted;
    }

    // MemberCreateServiceDto로 변환하는 메서드
    public MemberServiceDto toMemberCreateServiceDto() {
        return new MemberServiceDto(null, password, email, member_type, insurance_status, return_deposit,
            safety_score, profile_image, progress_step, created_at, updated_at, is_deleted);
    }

    public MemberServiceDto toMemberCreateServiceDto(Long id) {
        return new MemberServiceDto(id, password, email, member_type, insurance_status, return_deposit,
            safety_score, profile_image, progress_step, created_at, updated_at, is_deleted);
    }
}
