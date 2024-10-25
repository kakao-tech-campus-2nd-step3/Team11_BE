package boomerang.template.domain;

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
@Table(name = "template")
public class TemplateDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private TemplateColumn1 templateColumn1;

    @Embedded
    private TemplateColumn2 templateColumn2;

    public TemplateDomain() {
    }

    public TemplateDomain(Long id, TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.id = id;
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        TemplateDomain item = (TemplateDomain) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
