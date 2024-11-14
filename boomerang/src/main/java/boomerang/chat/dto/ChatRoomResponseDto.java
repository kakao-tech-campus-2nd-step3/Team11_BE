package boomerang.chat.dto;

import boomerang.chat.domain.ChatRoom;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ChatRoomResponseDto {

    private Long id;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
    }
}
