package boomerang.board.dto;

import boomerang.board.domain.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponseDto {
    private Long id;
    private String title;
    private String subtitle;
    private String content;
    private BoardType boardType;
    private Location location;
    private AnonymousStatus anonymousStatus;
    private String writerEmail;

    public BoardResponseDto(Long id, String title, String subtitle, String content, BoardType boardType, Location location, AnonymousStatus anonymousStatus, String writerEmail) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
        this.writerEmail = writerEmail;
    }

    // Board 도메인 객체를 받아서 BoardResponseDto를 생성하는 생성자
    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.subtitle = board.getSubtitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
        this.location = board.getLocation();
        this.anonymousStatus = board.getAnonymousStatus();
        this.writerEmail = board.getMember().getEmail();
    }
}
