package boomerang.domain.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

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
    private Member_type member_type;

    @Embedded
    private Insurance_status insurance_status;

    @Embedded
    private Return_deposit return_deposit;

    @Embedded
    private Safety_score safety_score;

    @Embedded
    private Profile_image profile_image;

    @Embedded
    private Progress_step progress_step;

    @Embedded
    private Created_at created_at;

    @Embedded
    private Updated_at updated_at;

    @Embedded
    private Is_deleted is_deleted;

    protected MemberDomain() {
    }

    public MemberDomain(Long id, Password password, Email email, Member_type member_type,
        Insurance_status insurance_status, Return_deposit return_deposit, Safety_score safety_score, Profile_image profile_image,
        Progress_step progress_step, Created_at created_at, Updated_at updated_at, Is_deleted is_deleted) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.member_type = member_type;
        this.insurance_status = insurance_status;
        this.return_deposit = return_deposit;
        this.safety_score = safety_score;
        this.profile_image = profile_image;
        this.progress_step = progress_step;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.is_deleted = is_deleted;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        MemberDomain item = (MemberDomain) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
