package boomerang.mentor.dto;

import boomerang.mentor.domain.MentorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MentorCreateRequestDto {

    @NotNull(message = "멘토 유형은 필수입니다.")
    private final MentorType mentorType;

    @NotBlank(message = "경력 정보는 필수입니다.")
    @Size(min = 10, max = 500, message = "경력 정보는 10자 이상 500자 이하여야 합니다.")
    private final String career;

    @NotBlank(message = "자기소개는 필수입니다.")
    @Size(min = 50, max = 1000, message = "자기소개는 50자 이상 1000자 이하여야 합니다.")
    private final String introduce;

    private final Boolean advertisementStatus;

    @NotBlank(message = "연락처는 필수입니다.")
    @Size(min = 10, max = 20, message = "연락처는 10자 이상 20자 이하여야 합니다.")
    private final String contact;

    public MentorCreateRequestDto(MentorType mentorType, String career, String introduce, Boolean advertisementStatus, String contact) {
        this.mentorType = mentorType;
        this.career = career;
        this.introduce = introduce;
        this.advertisementStatus = advertisementStatus != null ? advertisementStatus : false;
        this.contact = contact;
    }
}
