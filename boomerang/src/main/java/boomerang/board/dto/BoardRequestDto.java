package boomerang.board.dto;

import boomerang.board.domain.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BoardRequestDto {
    private Title title;
    private Subtitle subtitle;
    private Content content;
    private BoardType boardType;
    private Location location;
    private AnonymousStatus anonymousStatus;

    // 생성자
    public BoardRequestDto(Title title, Subtitle subtitle, Content content, BoardType boardType, Location location, AnonymousStatus anonymousStatus) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
    }

    // Board 도메인 객체로 변환하는 메서드
    public Board toBoard() {
        return Board.builder()
                .title(title)
                .subtitle(subtitle)
                .content(content)
                .boardType(boardType)
                .location(location)
                .anonymousStatus(anonymousStatus)
                .build();
    }

    // 기존 ID를 사용하여 Board 객체를 생성하는 메서드
    public Board toBoard(Long id) {
        return Board.builder()
                .id(id)
                .title(title)
                .subtitle(subtitle)
                .content(content)
                .boardType(boardType)
                .location(location)
                .anonymousStatus(anonymousStatus)
                .build();
    }
}
