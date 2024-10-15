package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import jakarta.persistence.*;
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

    @OneToOne
    @JoinColumn(name = "author_id")
    private Member member;

    //이
    @Embedded
    private MainStep1 mainStep1;

    //임차권 등기 명령 - C,D

    //보증 이행 청구

    //지급 명령 신청

    //보증 이행 청구

    //전세 보증금 반환 소송

    public Progress(Member member) {
        this.member = member;
        //이후에 타입별로 구분해서 생성하는게 필요할 것 같음
        this.mainStep1 = new MainStep1();

    }


    public void updateStep(SubStep subStep, boolean status) {
        switch (subStep) {
            case SUB_STEP_1:
                this.mainStep1.updateSubStep1(status);
                break;
            case SUB_STEP_2:
                this.mainStep1.updateSubStep2(status);
                break;
        }
    }

    public boolean getSubStepStatus(SubStep subStep) {
        switch (subStep) {
            case SUB_STEP_1:
                return mainStep1.getSubStep1();
            case SUB_STEP_2:
                return mainStep1.getSubStep2();
            default:
                throw new BusinessException(ErrorCode.PROGRESS_REQUEST_ERROR);
        }

    }
}
