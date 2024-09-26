package boomerang.domain.comment.controller;

import boomerang.domain.comment.dto.CommentRequestDto;
import boomerang.domain.comment.service.CommentService;
import boomerang.domain.member.domain.Member;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    //댓글 조회


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


    //댓글 삭제


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(BusinessException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }

}
