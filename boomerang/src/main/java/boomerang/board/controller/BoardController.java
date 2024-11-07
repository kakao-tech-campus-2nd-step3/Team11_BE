package boomerang.board.controller;

import boomerang.board.domain.Board;
import boomerang.board.dto.BoardBestListRequestDto;
import boomerang.board.dto.BoardDetailResponseDto;
import boomerang.board.dto.BoardListRequestDto;
import boomerang.board.dto.BoardRequestDto;
import boomerang.board.dto.BoardResponseDto;
import boomerang.board.dto.BoardSimpleResponseDto;
import boomerang.board.service.BoardService;
import boomerang.comment.dto.CommentListRequestDto;
import boomerang.comment.dto.CommentResponseDto;
import boomerang.comment.service.CommentService;
import boomerang.global.exception.DomainValidationException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.response.PageResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.like.service.LikeService;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
    public ResponseEntity<PageResponseDto<BoardResponseDto>> getBestBoards(
        @ModelAttribute BoardBestListRequestDto boardBestListRequestDto) {

        Page<Board> boardPage = boardService.getBestBoards(boardBestListRequestDto);
        Page<BoardResponseDto> boardResponsePage = boardPage.map(BoardResponseDto::new);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new PageResponseDto<>(boardResponsePage));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<BoardResponseDto>> getAllBoards(
        @ModelAttribute BoardListRequestDto boardListRequestDto) {

        Page<Board> boardPage = boardService.getAllBoards(boardListRequestDto);
        Page<BoardResponseDto> boardResponsePage = boardPage.map(BoardResponseDto::new);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new PageResponseDto<>(boardResponsePage));
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<BoardDetailResponseDto> getBoardById(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId) {

        Board board = boardService.getBoard(boardId);
        PageResponseDto<CommentResponseDto> commentListResponseDto = new PageResponseDto<>(
            commentService.getAllComment(boardId, new CommentListRequestDto())
                .map(CommentResponseDto::new));

        boolean isLiked = false;

        if (principalDetails != null) {
            Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
            isLiked = likeService.isLikedByMember(board, member);
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(new BoardDetailResponseDto(board, commentListResponseDto, isLiked));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardSimpleResponseDto> createBoard(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestPart("data") BoardRequestDto boardRequestDto,
        @RequestParam("content") String content,
        @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        boardRequestDto.setContent(content);
        Board createdBoard = boardService.createBoard(boardRequestDto, member, images);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new BoardSimpleResponseDto(createdBoard, false));
    }

    @PutMapping("/{board_id}")
    public ResponseEntity<BoardSimpleResponseDto> updateBoard(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId,
        @RequestPart("data") BoardRequestDto boardRequestDto,
        @RequestParam("content") String content,
        @RequestParam(value = "images", required = false) List<MultipartFile> images
    ) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        boardRequestDto.setContent(content);
        Board updatedBoard = boardService.updateBoard(boardId, boardRequestDto, member, images);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new BoardSimpleResponseDto(updatedBoard, false));
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
