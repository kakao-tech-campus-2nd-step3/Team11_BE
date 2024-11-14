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

    private Long id;
    private String mentorNickName;
    private String menteeNickName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH")
    private LocalDateTime consultationDateTime;
    private ConsultationStatus consultationStatus;
    private String title;
    private String content;

    public ConsultationResponseDto(Consultation consultation) {
        this.id = consultation.getId();
        this.mentorNickName = consultation.getMentorNickname();
        this.menteeNickName = consultation.getMenteeNickname();
        this.consultationDateTime = consultation.getConsultationDateTime();
        this.consultationStatus = consultation.getConsultationStatus();
        this.title = consultation.getTitle();
        this.content = consultation.getContent();
    }
}

