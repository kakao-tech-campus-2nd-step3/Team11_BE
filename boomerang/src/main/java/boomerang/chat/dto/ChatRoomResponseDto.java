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
        // 테스트를 위해 주석처리
//        this.creatorName = chatRoom.getCreator().getNickname();
        this.createdAt = chatRoom.getCreatedAt();
    }
}
