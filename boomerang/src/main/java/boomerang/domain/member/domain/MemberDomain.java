package boomerang.domain.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "member")
@Getter
@Setter
public class MemberDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Password password;

    @Embedded
    private Email email;

    @Embedded
    private MemberType memberType;

    //보험가입여부
    @Column(name = "insurance_status")
    private boolean insuranceStatus;

    //돌려받을 수 있는 보증금
    @Embedded
    private ReturnDeposit returnDeposit;

    @Embedded
    private SafetyScore safetyScore;

    @Embedded
    private ProfileImage profileImage;

    @Embedded
    private ProgressStep progressStep;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    protected MemberDomain() {
    }

    public MemberDomain(Password password, Email email) {
        this.password = password;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MemberDomain that = (MemberDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
