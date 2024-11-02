package boomerang.board.dto;

import boomerang.board.domain.AnonymousStatus;
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
    private AnonymousStatus anonymousStatus;
    private String writerEmail;
    private String writerName;
    private Long likeCount;
    private Long commentCount;

    public BoardResponseDto(Board board, int contentLength) {

        // content 길이를 제한하고, contentLength보다 길 경우 '...'을 추가
        String content = board.getContent();
        if (content.length() > contentLength) {
            content = content.substring(0, contentLength) + "...";
        }

        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writerEmail = board.getWriterEmail();
        this.writerName = board.getWriterName();
        this.anonymousStatus = board.getAnonymousStatus();
        this.likeCount = board.getLikeCount();
        this.commentCount = board.getCommentCount();
    }
}
