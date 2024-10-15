package boomerang.progress.domain;

import boomerang.member.domain.Member;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.util.ProgressStrategy;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

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

    //이
    @Embedded
    private MainStepEx mainStepEx;

    //임차권 등기 명령 - C,D

    //보증 이행 청구

    //지급 명령 신청

    //보증 이행 청구

    //전세 보증금 반환 소송

    public Progress(Member member, ProgressType progressType) {
        this.member = member;
        this.progressType = progressType;
        //이후에 타입별로 구분해서 생성하는게 필요할 것 같음
        if (progressType.equals(ProgressType.A)) {
            this.mainStepEx = new MainStepEx();
        }
    }

    public List<MainStep> getActiveMainStepList() {
        return ProgressStrategy.generateActiveMainStepsByType(this);
    }

    public MainStep findMainStepByEnum(MainStepEnum mainStepEnum) {
        return ProgressStrategy.findMainStepByEnum(this,mainStepEnum);
    }

    public SubStepResponseDto findSubStepByEnum(SubStepEnum subStepEnum) {
        return ProgressStrategy.findSubStepByEnum(this,subStepEnum);
    }
}
