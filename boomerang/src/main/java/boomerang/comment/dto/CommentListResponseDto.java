package boomerang.comment.dto;

import boomerang.comment.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentListResponseDto {
    private int totalPage;
    private int currentPage;
    private List<CommentResponseDto> commentResponseDtoList;

    public CommentListResponseDto(Page<Comment> page) {
        this.totalPage = page.getTotalPages();
        this.currentPage = page.getNumber() + 1;
        this.commentResponseDtoList = page.stream()
                .map(CommentResponseDto::new)
                .toList();
    }
}
