package boomerang.progress.domain;

import boomerang.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProgressB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean step1;
    private boolean step2;
    private boolean step3;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate
    private LocalDateTime createdAt; //기본 업데이트 됨

    @LastModifiedDate
    private LocalDateTime updatedAt; //기본 업데이트 됨

    public ProgressB(Member member) {
        this.member = member;
    }
}
