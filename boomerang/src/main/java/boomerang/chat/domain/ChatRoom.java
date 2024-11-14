package boomerang.chat.domain;

import boomerang.IsDeleted;
import boomerang.consultation.domain.Consultation;
import boomerang.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "chat_room")
public class ChatRoom {

    // 상담의 id 값을 받아 채팅방 아이디를 생성
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mento", nullable = false)
    private Member mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee", nullable = false)
    private Member mentee;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private IsDeleted isDeleted;

    protected ChatRoom() {
    }

    public ChatRoom(Consultation consultation) {
        this.id = consultation.getId();
        this.mentor = consultation.getMentor().getMember();
        this.mentee = consultation.getMentee();
    }
}
