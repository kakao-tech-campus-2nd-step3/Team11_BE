package boomerang.consultation.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Member mentee;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus consultationStatus;

    private LocalDateTime consultationDateTime;

    private String title;

    private String content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Consultation(Member mentee, Mentor mentor, LocalDateTime consultationDateTime, String title, String content) {
        this.mentee = mentee;
        this.mentor = mentor;
        this.consultationDateTime = consultationDateTime;
        this.title = title;
        this.content = content;
        this.consultationStatus = ConsultationStatus.RECEIVED;
    }

    public long getMentorId() {
        return this.mentor.getMember().getId();
    }

    public String getMentorNickname() {
        return this.mentor.getMember().getNickname();
    }

    public String getMenteeNickname() {
        return this.mentee.getNickname();
    }

    public long getMenteeId() {
        return this.mentee.getId();
    }

    public boolean isNotMentor(Mentor mentor) {
        return !this.mentor.equals(mentor);
    }

    public boolean isNotMentee(Member mentee) {
        return !this.mentee.equals(mentee);
    }

    public void validateOnGoing() {
        if (this.consultationStatus != ConsultationStatus.ONGOING)
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_ONGOING);
    }

    public void confirm() {
        this.consultationStatus = ConsultationStatus.PENDING;
    }

    public void start() {
        this.consultationStatus = ConsultationStatus.ONGOING;
    }

    public void complete() {
        this.consultationStatus = ConsultationStatus.FINISHED;
    }

    public void validateReceived() {
        if (this.consultationStatus != ConsultationStatus.RECEIVED) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_RECEIVED);
        }
    }

    public void validateOngoing() {
        if (this.consultationStatus != ConsultationStatus.ONGOING) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_ONGOING);
        }
    }

    public boolean isConfirmed() {
        return this.consultationStatus.equals(ConsultationStatus.PENDING);
    }

    public boolean isFinished() {
        return this.consultationStatus.equals(ConsultationStatus.FINISHED);
    }

    public void makeSchedule(LocalDateTime localDateTime) {
        this.consultationDateTime = localDateTime;
    }

    // 해당 멤버가 상담에 속한 멤버인지 검증
    public void validateMemberIsParticipant(Member member) {
        if (!this.mentee.equals(member) && !this.mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_MEMBER_IS_NOT_PARTICIPANT);
        }
    }
}

