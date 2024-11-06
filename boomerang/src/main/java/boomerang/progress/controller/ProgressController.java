package boomerang.progress.controller;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.MainStepEnum;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.dto.ProgressByMainResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.ProgressTypeResponseDto;
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

    @PostMapping("/progress/type")//타입 검사
    public ResponseEntity<ProgressTypeResponseDto> checkUserType(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody ProgressTypeRequestDto progressTypeRequestDto) {
        ProgressType progressTypeOfMember = progressService.checkUserType(principalDetails, progressTypeRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(new ProgressTypeResponseDto(progressTypeOfMember));
    }

    @GetMapping("/progress/type")//유저의 타입 정보
    public ResponseEntity<ProgressTypeResponseDto> getUserType(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ProgressType progressTypeOfMember = progressService.getUserType(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(new ProgressTypeResponseDto(progressTypeOfMember));
    }

    //유저의 메인 단계 목록 조회
    @GetMapping("/progress")
    public ResponseEntity<ProgressByMainResponseDto > getProgressDetails(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        ProgressByMainResponseDto progressByMainResponseDto = progressService.getProgressDetails(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(progressByMainResponseDto);
    }

    //유저의 특정 메인 단계의 서브 단계 목록 조회
    @GetMapping("/progress/{main}")
    public ResponseEntity<ProgressByMainResponseDto> getSubStepsByMainStep(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                   @PathVariable("main") MainStepEnum mainStepEnum) {
        ProgressByMainResponseDto progressByMainResponseDto = progressService.getSubStepsByMainStep(principalDetails, mainStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(progressByMainResponseDto);
    }

    //유저의 메인 단계의 서브 단계 목록 조회
    @GetMapping("/progress/{main}/{sub}")
    public ResponseEntity<SubStepResponseDto> getSubStepStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("main") MainStepEnum mainStepEnum,
                                              @PathVariable("sub") SubStepEnum subStepEnum) {
        validMatchingOfMainStepAndSubStep(mainStepEnum, subStepEnum);
        SubStepResponseDto subStepResponseDto = progressService.getSubStepStatus(principalDetails, mainStepEnum, subStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(subStepResponseDto);
    }

    @PostMapping("/progress/{main}/{sub}")//특정 서브단계 완료로 변경
    public ResponseEntity<SubStepResponseDto> completeProgress(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("main") MainStepEnum mainStepEnum,
                                              @PathVariable("sub") SubStepEnum subStepEnum) {
        validMatchingOfMainStepAndSubStep(mainStepEnum, subStepEnum);
        SubStepResponseDto subStepResponseDto = progressService.completeProgress(principalDetails, mainStepEnum, subStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(subStepResponseDto);
    }

    @DeleteMapping("/progress/{main}/{sub}")//특정 서브단계 미완료로 변경
    public ResponseEntity<SubStepResponseDto> revertProgressToIncomplete(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                        @PathVariable("main") MainStepEnum mainStepEnum,
                                                        @PathVariable("sub") SubStepEnum subStepEnum) {
        validMatchingOfMainStepAndSubStep(mainStepEnum, subStepEnum);
        SubStepResponseDto subStepResponseDto = progressService.revertProgressToIncomplete(principalDetails, mainStepEnum, subStepEnum);
        return ResponseEntity.status(HttpStatus.OK).body(subStepResponseDto);
    }

    private void validMatchingOfMainStepAndSubStep(MainStepEnum mainStepEnum, SubStepEnum subStepEnum) {
        if (!mainStepEnum.isMatchingMainAndSub(subStepEnum)) {
            throw new BusinessException(ErrorCode.PROGRESS_SUB_MAIN_DO_NOT_MATCH);
        }
    }

}
