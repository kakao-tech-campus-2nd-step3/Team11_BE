package boomerang.consultation.controller;

import boomerang.consultation.dto.ConsultationRequestDto;
import boomerang.consultation.dto.ConsultationResponseDto;
import boomerang.consultation.service.ConsultationService;
import boomerang.global.oauth.dto.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ConsultationController {

    private final ConsultationService consultationService;

    //상담등록
    @PostMapping("/consultation")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                       @RequestBody ConsultationRequestDto consultationRequestDto) {
        ConsultationResponseDto consultationResponseDto = consultationService.requestConsultation(principalDetails,consultationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationResponseDto);
    }

    //상담완료
    @PutMapping("/consultation/{consultation_id}")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                       @PathVariable("consultation_id") Long consultationId) {
        ConsultationResponseDto consultationResponseDto = consultationService.completeConsultation(principalDetails, consultationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationResponseDto);
    }

    //멘토별 상담 조회

    //개인별 상담 내역조회
    @GetMapping("user/consultation")
    public ResponseEntity<Page<ConsultationResponseDto>> getConsultationByUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return null;
    }

}
