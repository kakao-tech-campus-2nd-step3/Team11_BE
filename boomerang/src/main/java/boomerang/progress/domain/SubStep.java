package boomerang.progress.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class SubStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubStepEnum name;

    @ManyToOne
    @JoinColumn(name = "mainStep_id")
    private MainStep mainStep;

    private boolean completion;

    public String getName() {
        return this.name.getSubStepName();
    }

    public SubStepEnum getSubStepEnum() {
        return this.name;
    }

    public SubStep(MainStep mainStep, SubStepEnum name) {
        this.name = name;
        this.mainStep = mainStep;
        this.completion = false;
    }

    public void markAsComplete() {
        this.completion = true;
    }

    public void markAsIncomplete() {
        this.completion = false;
    }
}
