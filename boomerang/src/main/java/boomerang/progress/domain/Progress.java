package boomerang.progress.domain;

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

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member member;

    //계약 해지 내용 증명
    @Embedded
    private MainStep1 mainStep1;

    //임차권 등기 명령

    //보증 이행 청구

    //지급 명령 신청

    //보증 이행 청구

    //전세 보증금 반환 소송

}
