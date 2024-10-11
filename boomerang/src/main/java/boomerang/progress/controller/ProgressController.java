package boomerang.progress.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
