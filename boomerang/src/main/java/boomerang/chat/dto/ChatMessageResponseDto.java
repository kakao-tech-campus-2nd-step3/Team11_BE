package boomerang.chat.dto;

import boomerang.chat.domain.ChatMessage;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private Long id;
    private String nickname;
    private String message;
    private LocalDateTime createdAt;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.nickname = chatMessage.getSenderNickname();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
