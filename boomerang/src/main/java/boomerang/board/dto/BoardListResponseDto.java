package boomerang.board.dto;

import boomerang.board.domain.Board;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
public class BoardListResponseDto {
    // page와 같은 추가적인 정보를 담는 필드
    private String val;
    private List<BoardResponseDto> boardResponseDtoList;

    // 생성자: Board 리스트를 받아서 각 요소를 BoardResponseDto로 변환하여 List에 담는다
    public BoardListResponseDto(String val, List<Board> boardList) {
        this.val = val;
        this.boardResponseDtoList = boardList.stream()
                .map(BoardResponseDto::new)
                .toList();
    }
}
