package boomerang.progress.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.dto.ProgressResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    //이거 그냥 guideline 으로 할까요?
    @PostMapping("/progress/type-check")
    public ResponseEntity<?> checkUserType(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody ProgressTypeRequestDto progressTypeRequestDto) {
        ProgressType ProgressOfMember = progressService.checkUserType(principalDetails, progressTypeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ProgressOfMember);
    }

    @GetMapping("/progress/type")
    public ResponseEntity<?> getUserType(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ProgressType ProgressOfMember = progressService.getUserType(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(ProgressOfMember);
    }


    @GetMapping("/progress")
    public ResponseEntity<?> getUserProgress(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ProgressResponseDto progressResponseDto = progressService.getUserProgress(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(progressResponseDto);
    }


}
