package boomerang.board.dto;

import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import boomerang.board.domain.Location;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardSimpleResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writerNickname;
    private BoardType boardType;
    private Location location;
    private Long likeCount;
    private boolean isLiked;
    private Long commentCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public BoardSimpleResponseDto(Board board, boolean isLiked) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
        this.location = board.getLocation();
        this.writerNickname = board.getWriterNickname();
        this.likeCount = board.getLikeCount();
        this.isLiked = isLiked;
        this.commentCount = board.getCommentCount();
        this.createdAt = board.getCreatedAt();
    }
}