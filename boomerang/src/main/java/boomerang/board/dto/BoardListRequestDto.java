package boomerang.board.dto;

import boomerang.board.domain.Board;
import boomerang.board.domain.BoardType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@EqualsAndHashCode
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)은
// JSON 요청 바디에서의 직렬화/역직렬화에만 적용된다. 때문에 직접 필드명을 json 형태로 선언해주었다
public class BoardListRequestDto {
    private int page = 0;
    private int size = 10;
    private Sort.Direction sort_direction = Sort.Direction.DESC;
    private String sort_by = "id";
    private BoardType board_type;
    private int content_length;
}
