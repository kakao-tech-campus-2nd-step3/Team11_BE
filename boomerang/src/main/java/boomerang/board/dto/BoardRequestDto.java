package boomerang.board.dto;

import boomerang.board.domain.BoardType;
import boomerang.board.domain.Location;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BoardRequestDto {
    private String title;
    private String content;
    private BoardType board_type;
    private Location location;

    public BoardRequestDto() {}

    // 생성자
    public BoardRequestDto(String title, String content, BoardType board_type, Location location) {
        this.title = title;
        this.content = content;
        this.board_type = board_type;
        this.location = location;
    }

    public void setContentWithImageUrl(String content) {
        this.content = content;
    }
}
