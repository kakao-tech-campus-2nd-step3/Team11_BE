package boomerang.consultation.controller;

import boomerang.consultation.dto.*;
import boomerang.consultation.service.ConsultationService;
import boomerang.global.oauth.dto.PrincipalDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ConsultationController {

    private final ConsultationService consultationService;

    //상담신청
    @PostMapping("/consultation")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                       @Valid @RequestBody ConsultationRequestDto consultationRequestDto) {
        ConsultationResponseDto consultationResponseDto = consultationService.requestConsultation(principalDetails, consultationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultationResponseDto);
    }

    //일정등록
    @PostMapping("/consultation/schedule")
    public ResponseEntity<ScheduleResponseDto> registerSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto scheduleResponseDto = consultationService.registerSchedule(principalDetails, scheduleRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleResponseDto);
    }

    //일정 삭제
    @DeleteMapping("/consultation/schedule")
    public ResponseEntity<Void>  deleteSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails,@RequestBody ScheduleRequestDto scheduleRequestDto) {
        consultationService.deleteSchedule(principalDetails, scheduleRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //일정 조회
    @GetMapping("/consultation/schedule")
    public ResponseEntity<ScheduleResponseListDto> getSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails){
        ScheduleResponseListDto scheduleResponseListDto = consultationService.getScheduleByMentor(principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseListDto);
    }

    //상담완료
    @PutMapping("/consultation/{consultation_id}")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                       @PathVariable("consultation_id") Long consultationId) {
        ConsultationResponseDto consultationResponseDto = consultationService.completeConsultation(principalDetails, consultationId);
        return ResponseEntity.status(HttpStatus.OK).body(consultationResponseDto);
    }

    //상담조회
    @GetMapping("/consultation/{consultation_id}")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(@PathVariable("consultation_id") Long consultationId) {
        ConsultationResponseDto consultationResponseDto = consultationService.getConsultationDetail(consultationId);
        return ResponseEntity.status(HttpStatus.OK).body(consultationResponseDto);
    }


    //멘토별 상담 조회
    @GetMapping("mentor/{mentor_id}/consultation")
    public ResponseEntity<ConsultationResponseListDto> getConsultationOfMentor(@PathVariable("mentor_id") Long mentorId, Pageable pageable) {
        ConsultationResponseListDto consultation = consultationService.getConsultationOfMentor(mentorId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(consultation);
    }

    //개인별 상담 내역조회
    @GetMapping("memebr/consultation")
    public ResponseEntity<ConsultationResponseListDto> getConsultationOfUser(@AuthenticationPrincipal PrincipalDetails principalDetails, Pageable pageable) {
        ConsultationResponseListDto consultation = consultationService.getConsultationOfUser(principalDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(consultation);
    }

}
