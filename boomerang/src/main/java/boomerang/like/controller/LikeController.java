package boomerang.like.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.like.dto.LikeResponseDto;
import boomerang.like.service.LikeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board/")
public class LikeController {

    private final LikeService likeService;

    @GetMapping("/{board_id}/likes")
    public ResponseEntity<List<LikeResponseDto>> getLikesByBoardId(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId) {
        List<LikeResponseDto> likeResponses = likeService.getLikesByBoardId(principalDetails, boardId);
        return ResponseEntity.status(HttpStatus.OK).body(likeResponses);
    }

    @PostMapping("/{board_id}/likes")
    public ResponseEntity<LikeResponseDto> createLike(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId) {
        LikeResponseDto likeResponseDto = likeService.createLike(principalDetails, boardId);
        return ResponseEntity.status(HttpStatus.CREATED).body(likeResponseDto);
    }

    @DeleteMapping("/{board_id}/likes")
    public ResponseEntity<Void> deleteLike(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @PathVariable(name = "board_id") Long boardId) {
        likeService.deleteLike(principalDetails, boardId);
        return ResponseEntity.noContent().build();
    }
}