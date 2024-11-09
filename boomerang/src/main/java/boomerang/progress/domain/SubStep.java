package boomerang.progress.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    public SubStep(MainStep mainStep, SubStepEnum name) {
        this.name = name;
        this.mainStep = mainStep;
        this.completion = false;
    }

    public String getName() {
        return this.name.getSubStepName();
    }

    public SubStepEnum getSubStepEnum() {
        return this.name;
    }

    public void markAsComplete() {
        this.completion = true;
    }

    public void markAsIncomplete() {
        this.completion = false;
    }
}
