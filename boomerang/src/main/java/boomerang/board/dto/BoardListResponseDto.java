package boomerang.board.dto;

import boomerang.board.domain.Board;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@EqualsAndHashCode
public class BoardListResponseDto {
    // page와 같은 추가적인 정보를 담는 필드
    private String val;
    private List<BoardResponseDto> boardResponseDtoList;

    // 생성자
    public BoardListResponseDto(String val, List<BoardResponseDto> boardResponseDtoList) {
        this.val = val;
        this.boardResponseDtoList = boardResponseDtoList;
    }

    /*
        Domain 리스트를 받아서 각 요소를 TemplateResponseDto로 변환하여 List에 담는다
    */
    public static BoardListResponseDto of(List<Board> boardList) {
        List<BoardResponseDto> boardResponseDtoList = boardList.stream()
                .map(BoardResponseDto::of)
                .toList();

        return BoardListResponseDto.builder()
                .val("some_value")  // 필요에 따라 값 설정
                .boardResponseDtoList(boardResponseDtoList)
                .build();
    }
}
