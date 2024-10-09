package boomerang.like.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.like.domain.Like;
import boomerang.like.dto.LikeResponseDto;
import boomerang.like.service.LikeService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board/")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/{board_id}/likes")
    public ResponseEntity<Page<LikeResponseDto>> getLikesByBoardId(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId,
        Pageable pageable) {
        Page<LikeResponseDto> likeResponsePage = likeService.getLikesByBoardId(principalDetails, boardId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(likeResponsePage);
    }
}
