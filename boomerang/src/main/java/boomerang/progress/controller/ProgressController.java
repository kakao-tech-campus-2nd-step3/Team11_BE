package boomerang.progress.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.progress.domain.MainStepEnum;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.dto.MainStepResponseDto;
import boomerang.progress.dto.ProgressDetailsResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.SubStepResponseDto;
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

    @PostMapping("/progress/type")
    public ResponseEntity<?> checkUserType(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody ProgressTypeRequestDto progressTypeRequestDto) {
        ProgressType progressTypeOfMember = progressService.checkUserType(principalDetails, progressTypeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(progressTypeOfMember);
    }

    @GetMapping("/progress/type")
    public ResponseEntity<?> getUserType(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ProgressType progressTypeOfMember = progressService.getUserType(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(progressTypeOfMember);
    }

    //유저의 메인 단계 목록 조회
    @GetMapping("/progress")
    public ResponseEntity<?> getProgressDetails(@AuthenticationPrincipal PrincipalDetails principalDetails){
        ProgressDetailsResponseDto progressDetailsResponseDto = progressService.getProgressDetails(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(progressDetailsResponseDto);
    }

    //유저의 특정 메인 단계의 서브 단계 목록 조회
    @GetMapping("/progress/{main}")
    public ResponseEntity<?> getSubStepsByMainStep(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("main") MainStepEnum mainStepEnum){
        MainStepResponseDto mainStepResponseDto = progressService.getSubStepsByMainStep(principalDetails, mainStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(mainStepResponseDto);
    }

    //유저의 메인 단계의 서브 단계 목록 조회
    @GetMapping("/progress/{main}/{sub}")
    public ResponseEntity<?> getSubStepStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("main") MainStepEnum mainStepEnum,
                                              @PathVariable("sub") SubStepEnum subStepEnum){
        SubStepResponseDto subStepResponseDto = progressService.getSubStepStatus(principalDetails, mainStepEnum, subStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(subStepResponseDto);
    }

    @PostMapping("/progress/{main}/{sub}")
    public ResponseEntity<?> completeProgress(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("main") MainStepEnum mainStepEnum,
                                              @PathVariable("sub") SubStepEnum subStepEnum) {

        SubStepResponseDto subStepResponseDto = progressService.completeProgress(principalDetails, mainStepEnum, subStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(subStepResponseDto);
    }


}
