package boomerang.board.controller;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardBestListRequestDto;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardListResponseDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.dto.BoardResponseDto;
import boomerang.board.service.BoardService;
import boomerang.comment.dto.CommentListRequestDto;
import boomerang.comment.dto.CommentListResponseDto;
import boomerang.comment.service.CommentService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.like.service.LikeService;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api/v1/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final LikeService likeService;

    public BoardController(BoardService boardService, MemberService memberService,
        CommentService commentService,
        LikeService likeService) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @GetMapping("/best")
    public ResponseEntity<BoardListResponseDto> getBestBoards(
        @ModelAttribute BoardBestListRequestDto boardBestListRequestDto) {
        Page<Board> boradPage = boardService.getBestBoards(boardBestListRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new BoardListResponseDto(boradPage, boardBestListRequestDto.getContent_length()));
    }

    @GetMapping
    public ResponseEntity<BoardListResponseDto> getAllBoards(
        @ModelAttribute BoardListRequestDto boardListRequestDto) {
        Page<Board> boradPage = boardService.getAllBoards(boardListRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new BoardListResponseDto(boradPage, boardListRequestDto.getContent_length()));
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<BoardResponseDto> getBoardById(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId) {
        Board board = boardService.getBoard(boardId);
        CommentListResponseDto commentListResponseDto =
            new CommentListResponseDto(
                commentService.getAllComment(boardId, new CommentListRequestDto()));

        boolean isLiked = false;

        if (principalDetails != null) {
            Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
            isLiked = likeService.isLikedByMember(board, member);
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(new BoardResponseDto(board, commentListResponseDto, isLiked));
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestBody BoardRequestDto boardRequestDto) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        boardService.createBoard(boardRequestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @PutMapping("/{board_id}")
    public ResponseEntity<Void> updateBoard(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId,
        @RequestBody BoardRequestDto boardRequestDto) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        boardService.updateBoard(boardId, boardRequestDto, member);

        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<Void> deleteBoard(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        boardService.deleteBoard(member, boardId);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(
        DomainValidationException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
