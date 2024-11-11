package boomerang.consultation.controller;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.dto.*;
import boomerang.consultation.service.ConsultationService;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
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
    private final MemberService memberService;

    //상담신청
    @PostMapping("/consultation")
    public ResponseEntity<ConsultationResponseDto> requestConsultation(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody ConsultationRequestDto consultationRequestDto
    ) {

        //상담 신청하는 사람이 멘티인지 검증
        if(memberService.getMemberByEmail(principalDetails.getMemberEmail()).getMentor() != null) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTEE);
        }

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

    //멘티가 멘토별 일정 조회
    @GetMapping("????/{mentor_id}")
    public ResponseEntity<ScheduleResponseListDto> getMentorSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails,@PathVariable("mentor_id") Long mentorId) {
        if(memberService.getMemberByEmail(principalDetails.getMemberEmail()).getMentor() != null) {
            throw new BusinessException(ErrorCode.MENTOR_ALREADY_EXISTS);
        }
        ScheduleResponseListDto scheduleResponseListDto = consultationService.getScheduleByMentorId(mentorId);
        return ResponseEntity.status(HttpStatus.OK).body(scheduleResponseListDto);
    }

    //상담상태 변경
    @PutMapping("/consultation/{consultation_id}")
    public ResponseEntity<ConsultationResponseDto> changeConsultationStatus(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                       @PathVariable("consultation_id") Long consultationId,
                                                                       @RequestBody ConsultationStatus consultationStatus) {
        if(memberService.getMemberByEmail(principalDetails.getMemberEmail()).getMentor() == null) {
            throw new BusinessException(ErrorCode.MENTOR_NOT_REGISTERED);
        }

        Consultation consultation = consultationService.getConsultation(consultationId);

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

    //멘토의 본인 상담 조회
    @GetMapping("?????????")
    public ResponseEntity<ConsultationResponseListDto> getConsultationsByMentor(@AuthenticationPrincipal PrincipalDetails principalDetails, Pageable pageable) {
        Mentor mentor = memberService.getMemberByEmail(principalDetails.getMemberEmail()).getMentor();
        if(mentor == null) {
            throw new BusinessException(ErrorCode.MENTOR_NOT_REGISTERED);
        }
        ConsultationResponseListDto consultation = consultationService.getConsultationOfMentor(mentor.getId(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(consultation);
    }

    //개인별 상담 내역조회
    @GetMapping("member/consultation")
    public ResponseEntity<ConsultationResponseListDto> getConsultationOfUser(@AuthenticationPrincipal PrincipalDetails principalDetails, Pageable pageable) {
        ConsultationResponseListDto consultation = consultationService.getConsultationOfUser(principalDetails, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(consultation);
    }

}
