package boomerang.progress.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.progress.domain.ProgressType;
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

    @PostMapping("/progress/type-check")
    public ResponseEntity<?> checkUserType(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody ProgressTypeRequestDto progressTypeRequestDto) {
        ProgressType progressTypeOfMember = progressService.checkUserType(principalDetails, progressTypeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(progressTypeOfMember);
    }

    @GetMapping("/progress/type")
    public ResponseEntity<?> checkUserType(@AuthenticationPrincipal PrincipalDetails principalDetails){
        ProgressType progressTypeOfMember = progressService.getUserType(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(progressTypeOfMember);
    }


}
