package boomerang.consultation.dto;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConsultationResponseDto {

    private String mentorNickName;                      //멤토닉네임
    private String menteeNickName;                      //멘티닉네임
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH")
    private LocalDateTime consultationDateTime;                 //상담일정
    private ConsultationStatus consultationStatus;      //상담상태
    private String title;
    private String content;

    public ConsultationResponseDto(Consultation consultation) {
        this.mentorNickName = consultation.getMentorNickname();
        this.menteeNickName = consultation.getMenteeNickname();
        this.consultationDateTime = consultation.getConsultationDateTime();
        this.consultationStatus = consultation.getConsultationStatus();
        this.title = consultation.getTitle();
        this.content = consultation.getContent();
    }
}

