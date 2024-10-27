package boomerang.board.dto;

import boomerang.board.domain.BoardType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)은
// JSON 요청 바디에서의 직렬화/역직렬화에만 적용된다. 때문에 직접 필드명을 json 형태로 선언해주었다
public class BoardBestListRequestDto {
    private int size = 10;
    private BoardType board_type;
    private int content_length;
}
