package boomerang.progress.domain;

import boomerang.progress.util.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SubStepInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private SubStepEnum subStepEnum;

    private String subStepName;

    @Column(length = 2000)
    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(length = 2000)
    private List<String> inputs = new ArrayList<>();

    public SubStepInfo(Long id, SubStepEnum subStepEnum, String subStepName, String content, List<String> inputs) {
        this.id = id;
        this.subStepEnum = subStepEnum;
        this.subStepName = subStepName;
        this.content = content;
        this.inputs = inputs;
    }
}

