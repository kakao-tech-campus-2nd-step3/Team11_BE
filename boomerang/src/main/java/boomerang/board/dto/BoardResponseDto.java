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
    private String summary;
    private String writerNickname;
    private Long likeCount;
    private Long commentCount;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.summary = board.getSummery();
        this.writerNickname = board.getWriterNickname();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
    }
}
