package boomerang.board.controller;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardListResponseDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.dto.BoardResponseDto;
import boomerang.board.service.BoardService;
import boomerang.domain.member.domain.Member;
import boomerang.domain.member.service.MemberService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    public BoardController(BoardService boardService, MemberService memberService) {
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @GetMapping()
    public ResponseEntity<BoardListResponseDto> getAllBoards(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute BoardListRequestDto boardListRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        List<Board> boardList = boardService.getAllBoards(boardListRequestDto);

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
    public ResponseEntity<Void> createBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody BoardRequestDto boardRequestDto) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        boardService.createBoard(boardRequestDto.toBoard(member));

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "id") Long id,
            @RequestBody BoardRequestDto boardRequestDto) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        boardService.updateBoard(boardRequestDto.toBoard(member, id));

        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable(name = "id") Long id) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        boardService.deleteBoard(member, id);
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
