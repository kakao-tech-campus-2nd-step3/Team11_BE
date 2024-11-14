package boomerang.chat.dto;

import boomerang.consultation.dto.ConsultationResponseDto;
import boomerang.global.response.PageResponseDto;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class ChatRoomResponseDto {
    private String mentorProfileImage;
    private String menteeProfileImage;
    private boolean isMentor;
    private ConsultationResponseDto consultationResponseDto;
    private PageResponseDto<ChatMessageResponseDto> chatMessageResponseDtoPage;


    public ChatRoomResponseDto(String mentorProfileImage, String menteeProfileImage, boolean isMentor,
                               ConsultationResponseDto consultationResponseDto, Page<ChatMessageResponseDto> chatMessageResponseDtoPage) {
        this.mentorProfileImage = mentorProfileImage;
        this.menteeProfileImage = menteeProfileImage;
        this.isMentor = isMentor;
        this.consultationResponseDto = consultationResponseDto;
        this.chatMessageResponseDtoPage = new PageResponseDto<>(chatMessageResponseDtoPage);
    }
}
