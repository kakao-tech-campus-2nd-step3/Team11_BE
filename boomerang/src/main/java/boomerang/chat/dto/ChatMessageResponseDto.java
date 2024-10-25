package boomerang.chat.dto;

import boomerang.chat.domain.ChatMessage;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ChatMessageResponseDto {
    private Long id;
    private String senderName;
    private String content;
    private LocalDateTime createdAt;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.senderName = chatMessage.getSender().getNickname();
        this.content = chatMessage.getContent();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
