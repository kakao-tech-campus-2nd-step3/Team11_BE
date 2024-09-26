package boomerang.like.controller;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import boomerang.like.domain.LikeDomain;
import boomerang.like.dto.LikeListResponseDto;
import boomerang.like.dto.LikeRequestDto;
import boomerang.like.dto.LikeResponseDto;
import boomerang.like.service.LikeService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/template")
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

    @GetMapping("/{id}")
    public ResponseEntity<LikeResponseDto> getLikeById(@PathVariable(name = "id") Long id) {
        LikeDomain likeDomain = likeService.getLikeDomainById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(LikeResponseDto.of(likeDomain));
    }

    @PostMapping()
    public ResponseEntity<Void> createTemplate(@RequestBody LikeRequestDto likeRequestDto) {
        likeService.createLikeDomain(likeRequestDto.toLikeDomain());
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(@PathVariable(name = "id") Long id, @RequestBody LikeRequestDto likeRequestDto) {
        likeService.updateLikeDomain(likeRequestDto.toLikeDomain(id));
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable(name = "id") Long id) {
        likeService.deleteLikeDomain(id);
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
