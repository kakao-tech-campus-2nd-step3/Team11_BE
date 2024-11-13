package boomerang.prevention.controller;

import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.prevention.dto.PreventionRequestDto;
import boomerang.prevention.dto.PreventionResponseDto;
import boomerang.prevention.service.PreventionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/prevention")
public class PreventionController {

    private final PreventionService preventionService;

    // 예방 설문 결과 저장
    @PostMapping
    public ResponseEntity<PreventionResponseDto> savePrevention(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @Valid @RequestBody PreventionRequestDto requestDto) {
        PreventionResponseDto responseDto = preventionService.savePrevention(
            principalDetails.getMemberEmail(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 예방 설문 결과 조회
    @GetMapping
    public ResponseEntity<PreventionResponseDto> getPrevention(
        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        PreventionResponseDto responseDto = preventionService.getPrevention(
            principalDetails.getMemberEmail());
        return ResponseEntity.ok(responseDto);
    }
}