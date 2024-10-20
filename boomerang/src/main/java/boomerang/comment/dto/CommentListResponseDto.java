package boomerang.comment.dto;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardResponseDto;
import boomerang.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

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
