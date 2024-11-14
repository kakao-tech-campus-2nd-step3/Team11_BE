package boomerang.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageListRequestDto {
    private int page = 0;
    private int size = 20;

    public ChatMessageListRequestDto(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
