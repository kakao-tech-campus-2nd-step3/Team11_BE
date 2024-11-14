package boomerang.chat.dto;

import boomerang.chat.domain.ChatMessage;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
public class ChatMessageResponseDto {

    private String nickname;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public ChatMessageResponseDto(ChatMessage chatMessage) {
        this.nickname = chatMessage.getSenderNickname();
        this.message = chatMessage.getMessage();
        this.createdAt = chatMessage.getCreatedAt();
    }
}
