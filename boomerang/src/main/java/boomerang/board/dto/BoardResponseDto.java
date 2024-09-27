package boomerang.board.dto;

import boomerang.board.domain.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponseDto {
    private Title title;
    private Subtitle subtitle;
    private Content content;
    private BoardType boardType;
    private Location location;
    private AnonymousStatus anonymousStatus;

    // 생성자
    public BoardResponseDto(Title title, Subtitle subtitle, Content content, BoardType boardType, Location location, AnonymousStatus anonymousStatus) {
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
        this.anonymousStatus = anonymousStatus;
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
                .build();
    }

    /*
        Domain 값에서 Response에 필요한 값들만 따로 추출하여 사용 가능

        Domain (id, title, subtitle, etc.) 이라면
        Response (title, subtitle) 와 같이 불필요한 부분을 제거 또는
        Response (title.getText() + "제목", subtitle.getText() + "부제목") 와 같이 값 형태를 변경하는 데 사용된다
     */
}
