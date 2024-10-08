package boomerang.board.dto;

import boomerang.board.domain.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
}
