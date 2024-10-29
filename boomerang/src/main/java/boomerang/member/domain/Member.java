package boomerang.member.domain;

import boomerang.IsDeleted;
import boomerang.kakao.domain.KakaoMember;
import boomerang.member.dto.MemberServiceDto;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "member")
@Getter
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    //보험가입여부
    @Column(name = "insurance_status")
    private boolean insuranceStatus;

    @Column(name = "nickname", unique = true)
    private String nickname;

    //돌려받을 수 있는 보증금
    @Embedded
    private ReturnDeposit returnDeposit;

    @Embedded
    private SafetyScore safetyScore;

    private String profileImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted")
    private IsDeleted isDeleted;

    @OneToOne(mappedBy = "member")
    private Progress progress;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    protected Member() {
    }


    public Member(MemberServiceDto memberServiceDto) {
        this.email = memberServiceDto.getEmail();
        this.nickname = memberServiceDto.getNickname();
        this.memberRole = MemberRole.COMPLETE_USER;
    }

    public Member(KakaoMember kakaoMember) {
        this.email = kakaoMember.email();
        this.memberRole = MemberRole.INCOMPLETE_USER;
    }

    public ProgressType getProgressType() {
        if (this.progress == null) {
            return null;
        }
        return this.progress.getProgressType();
    }

    public void registerProgress(Progress progress) {
        this.progress = progress;
    }

    public boolean isComplete() {
        return MemberRole.COMPLETE_USER.equals(this.memberRole);
    }

    public boolean hasProgress() {
        return this.progress != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member that = (Member) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
        this.memberRole = MemberRole.COMPLETE_USER;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }
}
