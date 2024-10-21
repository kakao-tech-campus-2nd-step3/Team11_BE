package boomerang.board.dto;

import boomerang.board.domain.AnonymousStatus;
import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import boomerang.board.domain.Location;
import boomerang.comment.dto.CommentListResponseDto;
import boomerang.comment.dto.CommentResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String writerEmail;
    private BoardType boardType;
    private Location location;
    private AnonymousStatus anonymousStatus;
    private Long likeCount;
    private Long commentCount;
    private CommentListResponseDto commentListResponseDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Board 도메인 객체를 받아서 BoardResponseDto를 생성하는 생성자
    public BoardResponseDto(Board board, CommentListResponseDto commentListResponseDto) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
        this.location = board.getLocation();
        this.anonymousStatus = board.getAnonymousStatus();
        this.writerEmail = board.getMember().getEmail();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
        this.commentListResponseDto = commentListResponseDto;
        this.createdAt = board.getCreatedAt();
    }
}
