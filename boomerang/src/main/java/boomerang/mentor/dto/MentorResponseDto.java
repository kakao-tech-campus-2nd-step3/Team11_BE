package boomerang.mentor.dto;

import boomerang.mentor.domain.Mentor;
import boomerang.mentor.domain.MentorType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MentorResponseDto {

    private final Long id;

    private final MentorType mentorType;

    private final String career;

    private final String introduce;

    private final Boolean advertisementStatus;

    private final String contact;

    public MentorResponseDto(Mentor mentor) {
        this.id = mentor.getId();
        this.mentorType = mentor.getMentorType();
        this.career = mentor.getCareer();
        this.introduce = mentor.getIntroduce();
        this.advertisementStatus = mentor.getAdvertisementStatus();
        this.contact = mentor.getContact();
    }
}
