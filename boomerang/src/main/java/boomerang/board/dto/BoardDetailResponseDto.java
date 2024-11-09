package boomerang.board.dto;

import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import boomerang.board.domain.Location;
import boomerang.comment.dto.CommentResponseDto;
import boomerang.global.response.PageResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerNickname;
    private BoardType boardType;
    private Location location;
    private Long likeCount;
    private boolean isLiked;
    private Long commentCount;
    private PageResponseDto<CommentResponseDto> commentListResponseDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;


    // Board 도메인 객체를 받아서 BoardResponseDto를 생성하는 생성자
    public BoardDetailResponseDto(Board board, PageResponseDto<CommentResponseDto> commentListResponseDto, boolean isLiked) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
        this.location = board.getLocation();
        this.writerNickname = board.getWriterNickname();
        this.likeCount = board.getLikeCount();
        this.isLiked = isLiked;
        this.commentCount = board.getCommentCount();
        this.commentListResponseDto = commentListResponseDto;
        this.createdAt = board.getCreatedAt();
    }
}
