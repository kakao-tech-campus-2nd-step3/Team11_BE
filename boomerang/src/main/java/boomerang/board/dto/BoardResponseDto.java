package boomerang.board.dto;

import boomerang.board.domain.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

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

    // Board 도메인 객체를 받아서 BoardResponseDto를 생성하는 생성자
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
        this.location = board.getLocation();
        this.anonymousStatus = board.getAnonymousStatus();
        this.writerEmail = board.getMember().getEmail();
        this.likeCount = board.getLikeCount(); // 좋아요 수
        this.commentCount = board.getCommentCount(); // 댓글 수
    }
}
