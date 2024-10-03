package boomerang.board.dto;

import boomerang.board.domain.*;
import boomerang.domain.member.domain.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BoardRequestDto {
    private String title;
    private String subtitle;
    private String content;
    private BoardType boardType;
    private Location location;
    private AnonymousStatus anonymousStatus;

    // 생성자
    public BoardRequestDto(String title, String subtitle, String content, BoardType boardType, Location location, AnonymousStatus anonymousStatus) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
    }

    public Board toBoard(Member member) {
        return Board.builder()
                .title(title)
                .subtitle(subtitle)
                .content(content)
                .boardType(boardType)
                .location(location)
                .anonymousStatus(anonymousStatus)
                .member(member)
                .build();
    }

    // 기존 ID를 사용하여 Board 객체를 생성하는 메서드
    public Board toBoard(Member member, Long id) {
        return Board.builder()
                .id(id)
                .title(title)
                .subtitle(subtitle)
                .content(content)
                .boardType(boardType)
                .location(location)
                .anonymousStatus(anonymousStatus)
                .member(member)
                .build();
    }
}
