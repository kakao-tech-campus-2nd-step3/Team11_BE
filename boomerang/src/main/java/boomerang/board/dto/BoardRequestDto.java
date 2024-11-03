package boomerang.board.dto;

import boomerang.board.domain.BoardType;
import boomerang.board.domain.Location;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardRequestDto {
    private String title;
    private String content;
    private BoardType boardType;
    private Location location;

    // 생성자
    public BoardRequestDto(String title, String content, BoardType boardType, Location location) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.location = location;
    }
}
