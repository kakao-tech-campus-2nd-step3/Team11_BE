package boomerang.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomListRequestDto {
    private int page = 0;
    private int size = 20;
}
