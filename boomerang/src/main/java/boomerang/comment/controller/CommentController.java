package boomerang.comment.controller;

import boomerang.comment.domain.Comment;
import boomerang.comment.dto.CommentListRequestDto;
import boomerang.comment.dto.CommentListResponseDto;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.service.CommentService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    //댓글 조회
    @GetMapping("/board/{board_id}/comments")
    public ResponseEntity<CommentListResponseDto> getAllComment(@PathVariable("board_id") Long boardId, CommentListRequestDto commentListRequestDto) {
        Page<Comment> commentPage = commentService.getAllComment(boardId, commentListRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new CommentListResponseDto(commentPage));
    }

    //댓글 생성
    @PostMapping("/board/{board_id}/comments")
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("board_id") Long boardId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        commentService.createComment(principalDetails.getMemberEmail(), boardId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //댓글 수정
    @PutMapping("/board/comments/{comment_id}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("comment_id") Long commentId,
                                              @Valid @RequestBody CommentRequestDto commentRequestDto) {
        commentService.updateComment(principalDetails.getMemberEmail(), commentId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //댓글 삭제
    @DeleteMapping("/board/comments/{comment_id}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("comment_id") Long commentId) {
        commentService.deleteComment(principalDetails.getMemberEmail(), commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(BusinessException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }

}
