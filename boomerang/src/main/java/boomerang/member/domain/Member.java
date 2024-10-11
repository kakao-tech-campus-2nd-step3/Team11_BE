package boomerang.member.domain;

import boomerang.IsDeleted;
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

    @Enumerated(value = EnumType.STRING)
    private ProgressType progressType;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private IsDeleted isDeleted;

    protected Member() {
    }

    public Member(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public void registerProgressType(ProgressType progressType) {
        this.progressType = progressType;
    }

    public boolean hasProgressType() {
        return this.progressType != null;
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
