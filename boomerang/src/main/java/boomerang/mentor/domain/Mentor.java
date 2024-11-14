package boomerang.mentor.domain;

import boomerang.consultation.domain.Schedule;
import boomerang.member.domain.Member;
import boomerang.mentor.dto.MentorCreateRequestDto;
import boomerang.mentor.dto.MentorUpdateRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    private Boolean advertisementStatus;

    private String contact;

    private Long score = 0L;

    private Long replyCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean isDeleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> scheduleList = new ArrayList<>();


    protected Mentor() {
    }

    public Mentor(MentorType mentorType, String career, String introduce,
        Boolean advertisementStatus, Member member, String contact) {
        this.mentorType = mentorType;
        this.career = career;
        this.introduce = introduce;
        this.advertisementStatus = advertisementStatus;
        this.member = member;
        this.contact = contact;
        this.isDeleted = false;  // 기본값으로 false 설정
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Mentor mentor = (Mentor) o;
        return Objects.equals(id, mentor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateMentor(MentorCreateRequestDto mentorCreateRequestDto) {
        this.mentorType = mentorCreateRequestDto.getMentorType();
        this.career = mentorCreateRequestDto.getCareer();
        this.introduce = mentorCreateRequestDto.getIntroduce();
        this.advertisementStatus = mentorCreateRequestDto.getAdvertisementStatus();
        this.isDeleted = false;
        this.contact = mentorCreateRequestDto.getContact();
    }

    public void updateMentor(MentorUpdateRequestDto mentorUpdateRequestDto) {
        this.mentorType = mentorUpdateRequestDto.getMentorType();
        this.career = mentorUpdateRequestDto.getCareer();
        this.introduce = mentorUpdateRequestDto.getIntroduce();
        this.advertisementStatus = mentorUpdateRequestDto.getAdvertisementStatus();
        this.isDeleted = false;
        this.contact = mentorUpdateRequestDto.getContact();
    }

    public void delete() {
        this.isDeleted = true;
    }

}
