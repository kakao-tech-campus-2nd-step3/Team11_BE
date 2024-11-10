package boomerang.chat.domain;

import boomerang.IsDeleted;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mento", nullable = false)
    private Member mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentee", nullable = false)
    private Member mentee;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private IsDeleted isDeleted;

    protected ChatRoom() {
    }

    public ChatRoom(Member mentor, Member mentee) {
        this.mentor = mentor;
        this.mentee = mentee;
    }
}
