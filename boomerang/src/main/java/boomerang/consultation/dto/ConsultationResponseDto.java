package boomerang.consultation.dto;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConsultationResponseDto {
    private long consultationId;                        //상담아이디
    private long mentorId;                              //멘토아이디
    private String mentorNickName;                      //멤토닉네임
    private long menteeId;                              //멘티아이디
    private String menteeNickName;                      //멘티닉네임
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate consultationDate;                 //상담일정
    private ConsultationStatus consultationStatus;      //상담상태

    public ConsultationResponseDto(Consultation consultation) {
        this.consultationId = consultation.getId();
        this.mentorId = consultation.getMentorId();
        this.mentorNickName = consultation.getMentorNickname();
        this.menteeId = consultation.getMenteeId();
        this.menteeNickName = consultation.getMenteeNickname();
        this.consultationDate = consultation.getConsultationDate();
        this.consultationStatus = consultation.getConsultationStatus();
    }
}

