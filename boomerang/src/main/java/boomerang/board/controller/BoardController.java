package boomerang.board.controller;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardListResponseDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.dto.BoardResponseDto;
import boomerang.board.service.BoardService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping()
    public ResponseEntity<BoardListResponseDto> getAllBoards() {
        List<Board> boardList = boardService.getAllBoards();
        return ResponseEntity.status(HttpStatus.OK)
                .body(BoardListResponseDto.of(boardList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable(name = "id") Long id) {
        Board board = boardService.getBoardById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BoardResponseDto.of(board));
    }

    @PostMapping()
    public ResponseEntity<Void> createBoard(@RequestBody BoardRequestDto boardRequestDto) {
        boardService.createBoard(boardRequestDto.toBoard());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(@PathVariable(name = "id") Long id, @RequestBody BoardRequestDto boardRequestDto) {
        boardService.updateBoard(boardRequestDto.toBoard(id));
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable(name = "id") Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
