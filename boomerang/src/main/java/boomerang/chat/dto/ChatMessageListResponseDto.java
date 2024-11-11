package boomerang.chat.dto;

import boomerang.chat.domain.ChatMessage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class ChatMessageListResponseDto {

    private final List<ChatMessageResponseDto> messages;
    private final int currentPage;
    private final int totalPages;

    public ChatMessageListResponseDto(Page<ChatMessage> chatMessagePage) {
        this.messages = chatMessagePage.stream()
            .map(ChatMessageResponseDto::new)
            .collect(Collectors.toList());
        this.currentPage = chatMessagePage.getNumber();
        this.totalPages = chatMessagePage.getTotalPages();
    }
}
