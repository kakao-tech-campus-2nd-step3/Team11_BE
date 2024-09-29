package boomerang.domain.member.dto;

import boomerang.domain.member.domain.Created_at;
import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Insurance_status;
import boomerang.domain.member.domain.Is_deleted;
import boomerang.domain.member.domain.MemberDomain;
import boomerang.domain.member.domain.MemberType;
import boomerang.domain.member.domain.Password;
import boomerang.domain.member.domain.ProfileImage;
import boomerang.domain.member.domain.ProgressStep;
import boomerang.domain.member.domain.ReturnDeposit;
import boomerang.domain.member.domain.SafetyScore;
import lombok.Getter;

@Getter
public class MemberServiceDto {
    private Long id;
    private Password password;
    private Email email;
    private MemberType memberType;
    private ReturnDeposit returnDeposit;
    private SafetyScore safetyScore;
    private ProfileImage profileImage;
    private ProgressStep progressStep;

    protected MemberServiceDto() {}

    // 생성자
    public MemberServiceDto(Long id, Password password, Email email, Member_type member_type,
        Insurance_status insurance_status, Return_deposit return_deposit, Safety_score safety_score, Profile_image profile_image,
        Progress_step progress_step, Created_at created_at, Updated_at updated_at, Is_deleted is_deleted) {
        this.id = id;
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
    public MemberDomain toMemberDomain() {

        return new MemberDomain(id, password, email, member_type, insurance_status, return_deposit,
            safety_score, profile_image, progress_step, created_at, updated_at, is_deleted);


    }
}
