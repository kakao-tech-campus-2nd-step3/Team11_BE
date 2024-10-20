package boomerang.board.dto;

import boomerang.board.domain.Board;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardListResponseDto {
    // page와 같은 추가적인 정보를 담는 필드
    private int page;
    private List<BoardResponseDto> boardResponseDtoList;

    // 생성자: Board 리스트를 받아서 각 요소를 BoardResponseDto로 변환하여 List에 담는다
    public BoardListResponseDto(Page<Board> boardPage) {
        this.page = boardPage.getNumber();
    }
}
