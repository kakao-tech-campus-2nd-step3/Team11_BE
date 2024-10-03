package boomerang.board.dto;

import boomerang.board.domain.*;
import boomerang.domain.member.domain.Email;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponseDto {
    private String title;
    private String subtitle;
    private String content;
    private BoardType boardType;
    private Location location;
    private AnonymousStatus anonymousStatus;
    private Email writerEmail;

    public BoardResponseDto(String title, String subtitle, String content, BoardType boardType, Location location, AnonymousStatus anonymousStatus, Email writerEmail) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
        this.writerEmail = writerEmail;
    }

    // Board 도메인 객체를 BoardResponseDto 객체로 변환하는 팩토리 메서드
    public static BoardResponseDto of(Board board) {
        return BoardResponseDto.builder()
                .title(board.getTitle())
                .subtitle(board.getSubtitle())
                .content(board.getContent())
                .boardType(board.getBoardType())
                .location(board.getLocation())
                .anonymousStatus(board.getAnonymousStatus())
                .writerEmail(board.getMember().getEmail())
                .build();
    }
}
