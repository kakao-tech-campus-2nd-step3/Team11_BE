package boomerang.board.dto;

import boomerang.board.domain.Board;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerEmail;
    private String writerName;
    private Long likeCount;
    private Long commentCount;

    public BoardResponseDto(Board board, int contentLength) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerEmail = board.getWriterEmail();
        this.writerName = board.getWriterName();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
    }
}
