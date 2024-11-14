package boomerang.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 외래키 연결 필요
    @Column(name = "chat_room_id", nullable = false)
    private Long chatRoomId;

    private String senderNickname;

    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    protected ChatMessage() {
    }

    public ChatMessage(Long chatRoomId, String senderNickname, String message, LocalDateTime createdAt) {
        this.chatRoomId = chatRoomId;
        this.senderNickname = senderNickname;
        this.message = message;
        this.createdAt = createdAt;
    }
}
