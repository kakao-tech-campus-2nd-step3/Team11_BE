package boomerang.chat.dto;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
public class ChatMessageListRequestDto {
    private int page = 0;
    private int size = 20;
    private Sort.Direction sortDirection = Sort.Direction.DESC;
    private String sortBy = "createdAt";

    public PageRequest toPageRequest() {
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }
}
