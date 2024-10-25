package boomerang.chat.dto;

import boomerang.chat.domain.ChatRoom;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ChatRoomResponseDto {
    private Long id;
    private String name;
    private String creatorName;
    private LocalDateTime createdAt;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.creatorName = chatRoom.getCreator().getNickname();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
