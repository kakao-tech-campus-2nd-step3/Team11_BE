package boomerang.progress.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MainStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @Enumerated(EnumType.STRING)
    private MainStepEnum name;

    @OneToMany(mappedBy = "mainStep", cascade = CascadeType.ALL)
    private List<SubStep> subStepList;

    public MainStep(MainStepEnum name, Progress progress) {
        this.name = name;
        this.progress = progress;
    }

    public String getName() {
        return this.name.getMainStepName();
    }

    public MainStepEnum getMainStepEnum() {
        return this.name;
    }

    public void registerSubStepList(List<SubStep> subStepList) {
        this.subStepList = subStepList;
    }


    public Optional<SubStep> getSubStepByEnum(SubStepEnum subStepEnum) {
        return this.subStepList.stream()
            .filter(subStep -> subStep.getSubStepEnum().equals(subStepEnum))
            .findFirst();
    }

    public boolean isCompletion() {
        return this.subStepList.stream()
            .allMatch(SubStep::isCompletion);
    }
}
