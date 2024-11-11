package boomerang.consultation.controller;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.dto.*;
import boomerang.consultation.service.ConsultationService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.PageResponseDto;
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
            @Valid @RequestBody ConsultationRequestDto consultationRequestDto
    ) {
        consultationService.requestConsultation(principalDetails, consultationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
            @PathVariable("consultation_id") Long consultationId
    ) {
        ConsultationResponseDto consultationResponseDto = consultationService.confirmConsultation(principalDetails, consultationId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(consultationResponseDto);
    }

    //상담상태 변경
    @PutMapping
    public ResponseEntity<ConsultationResponseDto> changeConsultationStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                            @PathVariable("consultation_id") Long consultationId,
                                                                            @RequestBody ConsultationStatus consultationStatus) {
        if (memberService.getMemberByEmail(principalDetails.getMemberEmail()).getMentor() == null) {
            throw new BusinessException(ErrorCode.MENTOR_NOT_REGISTERED);
        }

        Consultation consultation = consultationService.validateConsultationExists(consultationId);

        //현재상태가 확정전이며 변경하고자 하는 상태가 진행전이면 변경
        if ((consultation.getConsultationStatus() == ConsultationStatus.RECEIVED) && (consultationStatus == ConsultationStatus.PENDING)) {
            ConsultationResponseDto consultationResponseDto = consultationService.confirmConsultation(principalDetails, consultationId);
            return ResponseEntity.status(HttpStatus.OK).body(consultationResponseDto);
        }
        //현재상태가 진행전이며 변경하고자 하는 상태가 진행중이면 변경
        else if ((consultation.getConsultationStatus() == ConsultationStatus.PENDING) && (consultationStatus == ConsultationStatus.ONGOING)) {
            ConsultationResponseDto consultationResponseDto = consultationService.startConsultationMentor(principalDetails, consultationId);
            return ResponseEntity.status(HttpStatus.OK).body(consultationResponseDto);
        }
        //현재상태가 진행중이며 변경하고자 하는 상태가 상담완료이면 변경
        else if ((consultation.getConsultationStatus() == ConsultationStatus.ONGOING) && (consultationStatus == ConsultationStatus.FINISHED)) {
            ConsultationResponseDto consultationResponseDto = consultationService.completeConsultation(principalDetails, consultationId);
            return ResponseEntity.status(HttpStatus.OK).body(consultationResponseDto);
        }
        //단계를 건너뛰려고 하거나 확전전인데 다른 상태에서 이전단계로 돌아가려고 하면 에러
        else
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_CHANGED);

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
