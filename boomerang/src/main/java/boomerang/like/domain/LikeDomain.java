package boomerang.like.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Entity
@Builder
@Table(name = "like")
public class LikeDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private LikeColumn1 likeColumn1;

    @Embedded
    private LikeColumn2 likeColumn2;

    public LikeDomain() {
    }

    public LikeDomain(Long id, LikeColumn1 likeColumn1, LikeColumn2 likeColumn2) {
        this.id = id;
        this.likeColumn1 = likeColumn1;
        this.likeColumn2 = likeColumn2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        LikeDomain item = (LikeDomain) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
