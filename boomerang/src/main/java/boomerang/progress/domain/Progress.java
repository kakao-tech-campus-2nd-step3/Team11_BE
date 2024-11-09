package boomerang.progress.domain;

import boomerang.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)  //도메인에서 날짜필드가 자동 관리되도록 설정
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private ProgressType progressType;

    @OneToMany(mappedBy = "progress", cascade = CascadeType.ALL)
    private List<MainStep> mainStepList;

    public Progress(Member member, ProgressType progressType) {
        this.member = member;
        this.progressType = progressType;
    }

    public void registerMainStepList(List<MainStep> mainStepList) {
        this.mainStepList = mainStepList;
    }

    public Optional<MainStep> getMainStepByEnum(MainStepEnum mainStepEnum) {
        return this.mainStepList.stream()
            .filter(mainStep -> mainStep.getMainStepEnum().equals(mainStepEnum))
            .findFirst();
    }

}
