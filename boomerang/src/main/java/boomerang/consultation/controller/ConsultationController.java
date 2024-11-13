package boomerang.consultation.controller;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.dto.*;
import boomerang.consultation.service.ConsultationService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.PageResponseDto;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final MemberService memberService;

    //상담신청
    @PostMapping("/consultation")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody ConsultationRequestDto consultationRequestDto) {

        ConsultationResponseDto consultationResponseDto = consultationService.requestConsultation(principalDetails, consultationRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(consultationResponseDto);
    }

    //일정등록
    @PostMapping("/consultation/schedule")
    public ResponseEntity<ScheduleResponseDto> registerSchedule(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ScheduleRequestDto scheduleRequestDto) {
        ScheduleResponseDto scheduleResponseDto = consultationService.registerSchedule(
                principalDetails, scheduleRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleResponseDto);
    }

    //일정 삭제
    @DeleteMapping("/consultation/schedule")
    public ResponseEntity<Void> deleteSchedule(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ScheduleRequestDto scheduleRequestDto) {
        consultationService.deleteSchedule(principalDetails, scheduleRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //일정 조회
    @GetMapping("/consultation/schedule")
    public ResponseEntity<ScheduleResponseListDto> getSchedule(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ScheduleResponseListDto scheduleResponseListDto = consultationService.getScheduleByMentor(
                principalDetails);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseListDto);
    }

    //멘티가 멘토별 일정 조회
    @GetMapping("/consultation/schedule/{mentor_id}")
    public ResponseEntity<ScheduleResponseListDto> getMentorSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable("mentor_id") Long mentorId) {
        if (memberService.getMemberByEmail(principalDetails.getMemberEmail()).getMentor() != null) {
            throw new BusinessException(ErrorCode.MENTOR_ALREADY_EXISTS);
        }
        ScheduleResponseListDto scheduleResponseListDto = consultationService.getScheduleByMentorId(mentorId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseListDto);
    }

    //상담 확정하기
    @PutMapping("/consultation/{consultation_id}")
    public ResponseEntity<ConsultationResponseDto> confirmConsultation(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
    ) {
        ConsultationResponseDto consultationResponseDto = consultationService.confirmConsultation(principalDetails, consultationId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(consultationResponseDto);
    }



        return ResponseEntity.status(HttpStatus.OK).build();
    }

    }

    //개인별 상담 내역조회
    @GetMapping("/member/consultation")
    public ResponseEntity<PageResponseDto<ConsultationResponseDto>> getConsultationOfUser(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute ConsultationListRequestDto consultationListRequestDto
    ) {
        Page<Consultation> consultationPage = consultationService.getConsultationPage(
                principalDetails, consultationListRequestDto);
        Page<ConsultationResponseDto> consultationResponseDtoPage = consultationPage.map(ConsultationResponseDto::new);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new PageResponseDto<>(consultationResponseDtoPage));
    }

}
