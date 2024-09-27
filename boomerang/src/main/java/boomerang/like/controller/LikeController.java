package boomerang.like.controller;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.like.domain.LikeDomain;
import boomerang.like.dto.LikeListResponseDto;
import boomerang.like.service.LikeService;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board/")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping()
    public ResponseEntity<LikeListResponseDto> getAllLikes() {
        List<LikeDomain> likeDomainList = likeService.getAllLikeDomains();
        return ResponseEntity.status(HttpStatus.OK)
            .body(LikeListResponseDto.of(likeDomainList));
    }

    @GetMapping("/{board_id}")
    public ResponseEntity<LikeListResponseDto> getLikesByBoardId(
        @PathVariable(name = "board_id") Long boardId) {
        List<LikeDomain> likeDomainList = likeService.getLikeDomainsByBoardId(boardId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(LikeListResponseDto.of(likeDomainList));
    }

    @PostMapping("/{board_id}")
    public ResponseEntity<Void> createLike(Principal principal, @PathVariable(name = "board_id") Long boardId) {
        likeService.createLikeDomain(principal.getName(), boardId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/{board_id}")
    public ResponseEntity<Void> deleteLike(Principal principal, @PathVariable(name = "board_id") Long boardId) {
        likeService.deleteLikeDomain(principal.getName(), boardId);
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(
        DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}
