package boomerang.progress.domain;

import boomerang.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.Optional;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)  //도메인에서 날짜필드가 자동 관리되도록 설정
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
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
