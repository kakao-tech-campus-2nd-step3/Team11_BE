package boomerang.mentor.dto;

import boomerang.global.response.PageResponseDto;
import java.util.List;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class MentorInitialListResponseDto {

    private List<MentorResponseDto> recommendedMentors;
    private List<MentorResponseDto> expertMentors;
    private List<MentorResponseDto> normalMentors;
    private PageResponseDto<MentorResponseDto> mentors;

    public MentorInitialListResponseDto(Page<MentorResponseDto> recommendedMentorResponseDtoPage,
                                        Page<MentorResponseDto> expertMentorResponseDtoPage,
                                        Page<MentorResponseDto> normalMentorResponseDtoPage,
                                        Page<MentorResponseDto> mentorResponseDtoPage
    ) {
        this.recommendedMentors = recommendedMentorResponseDtoPage.getContent();
        this.expertMentors = expertMentorResponseDtoPage.getContent();
        this.normalMentors = normalMentorResponseDtoPage.getContent();
        this.mentors = new PageResponseDto<>(mentorResponseDtoPage);
    }
}
