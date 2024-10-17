package boomerang.mentor.domain;

import boomerang.IsDeleted;
import boomerang.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "mentor")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MentorType mentorType;

    private String career;

    private String introduce;

    private Boolean displayStatus;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private IsDeleted isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Mentor() {
    }

    public Mentor(MentorType mentorType, String career, String introduce, Boolean displayStatus, Member member) {
        this.mentorType = mentorType;
        this.career = career;
        this.introduce = introduce;
        this.displayStatus = displayStatus;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Mentor mentor = (Mentor) o;
        return Objects.equals(id, mentor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
