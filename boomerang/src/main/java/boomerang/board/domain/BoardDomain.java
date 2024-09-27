package boomerang.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@Table(name = "board")
public class BoardDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TemplateColumn1 templateColumn1;

    @Embedded
    private TemplateColumn2 templateColumn2;

    public BoardDomain() {
    }

    public BoardDomain(Long id, TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.id = id;
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        BoardDomain item = (BoardDomain) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}