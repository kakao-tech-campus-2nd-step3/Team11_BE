package boomerang.progress.domain;

import boomerang.progress.util.StringListConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SubStepInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubStepEnum subStepEnum;

    private String subStepName;

    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(length = 2000)
    private List<String> inputs = new ArrayList<>();
}

