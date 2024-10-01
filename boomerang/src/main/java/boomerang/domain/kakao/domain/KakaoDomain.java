package boomerang.domain.kakao.domain;

import jakarta.persistence.Column;
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
@Table(name = "kakao")
@Getter
@Setter
public class KakaoDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오 내 앱키로 고정된 값
    @Column(unique = true, nullable = false)
    private String client_id = "5c13fd3c6832cef54c183d9295eecacb";


    @Column(unique = true, nullable = false)
    private String redirect_url = "http://localhost:8080";


    // 카카오에서 고정 값을 "authorization_code"로 정해준다.
    @Column(unique = true, nullable = false)
    private String grant_type = "authorization_code";

    @Column(unique = true, nullable = false)
    private String code;

    public KakaoDomain() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        KakaoDomain item = (KakaoDomain) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
