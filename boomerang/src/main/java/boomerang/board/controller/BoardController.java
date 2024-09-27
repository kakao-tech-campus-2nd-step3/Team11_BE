package boomerang.board.controller;

import boomerang.board.domain.BoardDomain;
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
    public ResponseEntity<BoardListResponseDto> getAllTemplates() {
        List<BoardDomain> boardDomainList = boardService.getAllTemplateDomains();
        return ResponseEntity.status(HttpStatus.OK)
                .body(BoardListResponseDto.of(boardDomainList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getTemplateById(@PathVariable(name = "id") Long id) {
        BoardDomain boardDomain = boardService.getTemplateDomainById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BoardResponseDto.of(boardDomain));
    }

    @PostMapping()
    public ResponseEntity<Void> createTemplate(@RequestBody BoardRequestDto boardRequestDto) {
        boardService.createTemplateDomain(boardRequestDto.toTemplateDomain());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(@PathVariable(name = "id") Long id, @RequestBody BoardRequestDto boardRequestDto) {
        boardService.updateTemplateDomain(boardRequestDto.toTemplateDomain(id));
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable(name = "id") Long id) {
        boardService.deleteTemplateDomain(id);
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
