package boomerang.comment.controller;

import boomerang.comment.service.CommentService;
import boomerang.comment.dto.CommentRequestDto;
import boomerang.comment.dto.CommentResponseDto;
import boomerang.domain.member.domain.Member;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    //댓글 조회
    @GetMapping("comment/{board_id}")
    public ResponseEntity<?> getAllComment(//@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @PathVariable("board_id") Long boardId,
                                           Pageable pageable) {
        //현재 로그인 기능 미 구현으로 테스트 유저를 집어넣음 (이후 삭제 필요)
        Member tempMember = Member.builder().email("test123@bomerang.com").build();
        Page<CommentResponseDto> commentResponsePage = commentService.getAllComment(tempMember.getEmail(), boardId,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponsePage);
    }

    //댓글 생성
    @PostMapping("/comment/{board_id}")
    public ResponseEntity<?> createComment(//@AuthenticationPrincipal PrincipalDetails principalDetails
                                            @PathVariable("board_id") Long boardId,
                                            @RequestBody CommentRequestDto commentRequestDto) {
        //현재 로그인 기능 미 구현으로 테스트 유저를 집어넣음 (이후 삭제 필요)
        Member tempMember = Member.builder().email("test123@bomerang.com").build();
        commentService.createComment(tempMember.getEmail(), boardId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //댓글 수정
    @PutMapping("/comment/{board_id}/{comment_id}")
    public ResponseEntity<?> updateComment(//@AuthenticationPrincipal PrincipalDetails principalDetails
                                           @PathVariable("board_id") Long boardId,
                                           @PathVariable("comment_id") Long commentId,
                                           @RequestBody CommentRequestDto commentRequestDto) {
        //현재 로그인 기능 미 구현으로 테스트 유저를 집어넣음 (이후 삭제 필요)
        Member tempMember = Member.builder().email("test123@bomerang.com").build();
        commentService.updateComment(tempMember.getEmail(), commentId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //댓글 삭제
    @DeleteMapping("/comment/{board_id}/{comment_id}")
    public ResponseEntity<?> deleteComment(//@AuthenticationPrincipal PrincipalDetails principalDetails
                                           @PathVariable("board_id") Long boardId,
                                           @PathVariable("comment_id") Long commentId) {
        //현재 로그인 기능 미 구현으로 테스트 유저를 집어넣음 (이후 삭제 필요)
        Member tempMember = Member.builder().email("test123@bomerang.com").build();
        commentService.deleteComment(tempMember.getEmail(), commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(BusinessException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }

}
