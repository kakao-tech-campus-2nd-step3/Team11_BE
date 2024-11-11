package boomerang.consultation.domain;

import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public boolean isMentor(Mentor mentor) {
        return this.mentor.equals(mentor);
    }

    public boolean isMentee(Member mentee) {
        return this.mentee.equals(mentee);
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

    public boolean isConfirmed() {
        return this.consultationStatus.equals(ConsultationStatus.PENDING);
    }

    public boolean isFinished() {
        return this.consultationStatus.equals(ConsultationStatus.FINISHED);
    }

    public void makeSchedule(LocalDateTime localDateTime) {
        this.consultationDateTime = localDateTime;
    }
}

