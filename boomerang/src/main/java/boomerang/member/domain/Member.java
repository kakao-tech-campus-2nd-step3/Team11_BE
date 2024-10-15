package boomerang.member.domain;

import boomerang.IsDeleted;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "member")
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Embedded
    private MemberType memberType;

    //보험가입여부
    @Column(name = "insurance_status")
    private boolean insuranceStatus;

    @Column(name = "nickname")
    private String nickname;

    //돌려받을 수 있는 보증금
    @Embedded
    private ReturnDeposit returnDeposit;

    @Embedded
    private SafetyScore safetyScore;

    private String profileImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private IsDeleted isDeleted;

    @OneToOne(mappedBy = "member")
    private Progress progress;

    protected Member() {
    }

    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public ProgressType getProgressType() {
        if (this.progress == null) {
            return null;
        }
        return this.progress.getProgressType();
    }

    public void registerProgress(Progress progress) {
        this.progress = progress;
    }


    public boolean hasProgress() {
        return this.progress != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member that = (Member) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }



}
