package boomerang.comment.controller;

import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.dto.CommentResponseDto;
import boomerang.comment.service.CommentService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    //댓글 조회
    @GetMapping("/board/{board_id}/comments")
    public ResponseEntity<Page<CommentResponseDto>> getAllComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @PathVariable("board_id") Long boardId,
                                           Pageable pageable) {
        Page<CommentResponseDto> commentResponsePage = commentService.getAllComment(principalDetails, boardId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponsePage);
    }

    //댓글 생성
    @PostMapping("/board/{board_id}/comments")
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @PathVariable("board_id") Long boardId,
                                           @RequestBody CommentRequestDto commentRequestDto) {
        commentService.createComment(principalDetails.getMemberEmail(), boardId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //댓글 수정
    @PutMapping("/board/comments/{comment_id}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @PathVariable("comment_id") Long commentId,
                                           @RequestBody CommentRequestDto commentRequestDto) {
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
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }

}
