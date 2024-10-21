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

    private LocalDate consultationDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Consultation(Member mentee, Mentor mentor, LocalDate localDate) {
        this.mentee = mentee;
        this.mentor = mentor;
        this.consultationDate = localDate;
        this.consultationStatus= ConsultationStatus.PENDING;
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

    public void complete() {
        this.consultationStatus = ConsultationStatus.FINISHED;
    }
}

