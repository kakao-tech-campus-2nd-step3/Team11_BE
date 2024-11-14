package boomerang.consultation.service;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.Schedule;
import boomerang.consultation.dto.*;
import boomerang.consultation.repository.ConsultationRepository;
import boomerang.consultation.repository.ScheduleRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.service.MentorService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberService memberService;
    private final MentorService mentorService;

    @Transactional
    public ConsultationResponseDto requestConsultation(PrincipalDetails principalDetails,
        ConsultationRequestDto consultationRequestDto) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        validateMentee(mentee);
        Mentor mentor = mentorService.getMentor(consultationRequestDto.getMentorId());

        LocalDateTime localDateTime = consultationRequestDto.getConsultationDateTime();

        Schedule schedule = validateScheduleExists(mentor, localDateTime.toLocalDate());
        schedule.validateHourSlot(localDateTime.getHour());

        Consultation consultation = new Consultation(mentee, mentor, localDateTime,
                consultationRequestDto.getTitle(), consultationRequestDto.getContent());


        consultation.makeSchedule(localDateTime);
        schedule.unreserveHourSlot(localDateTime.getHour());

        consultationRepository.save(consultation);
        scheduleRepository.save(schedule);

        return new ConsultationResponseDto(consultation);
    }

    public ConsultationResponseDto confirmConsultation(PrincipalDetails principalDetails, Long consultationId) {
        Consultation consultation = validateConsultationExists(consultationId);
        consultation.validateReceived();

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        validateConsultationOwnership(member, consultation);

        consultation.confirm();
        consultationRepository.save(consultation);

        return new ConsultationResponseDto(consultation);
    }

    public void deleteConsultation(PrincipalDetails principalDetails, Long consultationId) {
        Consultation consultation = validateConsultationExists(consultationId);
        consultation.validateReceived();

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        validateConsultationOwnership(member, consultation);

        consultationRepository.delete(consultation);
    }

    public void finishConsultation(PrincipalDetails principalDetails, Long consultationId) {
        Consultation consultation = validateConsultationExists(consultationId);
        consultation.validateOngoing();

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        validateConsultationMember(member, consultation);

        consultation.complete();
        consultationRepository.save(consultation);
    }

    public void checkHour(int hour, List<Integer> hours) {
        if (hour < 0 || hour >= 24) {
            throw new BusinessException(ErrorCode.CONSULTATION_TIME_REQUEST_ERROR); // 0 ~ 23 범위에 벗어나는 시간 예외 처리
        }
    }


    @Transactional
    public ScheduleResponseDto registerSchedule(PrincipalDetails principalDetails,
        ScheduleRequestDto scheduleRequestDto) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();

        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }

        Map<String, List<Integer>> dayList = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : scheduleRequestDto.getList().entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey());
            Schedule schedule = scheduleRepository.findByMentorAndLocalDate(mentor, date)
                    .orElse(new Schedule(mentor, date));

            List<Integer> hours = entry.getValue();
            for (int hour : hours) {
                checkHour(hour, hours);
                if (schedule.getHourSlots().get(hour)) {
                    throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS);
                }
                schedule.reserveHourSlot(hour);
            }

            scheduleRepository.save(schedule);
            dayList.put(entry.getKey(), hours);
        }

        return new ScheduleResponseDto(dayList);
    }

    @Transactional
    public void deleteSchedule(PrincipalDetails principalDetails,
        ScheduleRequestDto scheduleRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();
        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        for (Map.Entry<String, List<Integer>> entry : scheduleRequestDto.getList().entrySet()) {
            LocalDate date = LocalDate.parse(entry.getKey()); // 문자열을 LocalDate로 변환
            Schedule schedule = scheduleRepository.findByMentorAndLocalDate(mentor, date)
                    .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR));

            List<Integer> hours = entry.getValue();
            for (int hour : hours) {
                checkHour(hour, hours); // 시간 범위 검증
                schedule.unreserveHourSlot(hour); // 시간 슬롯 예약 해제
            }

            scheduleRepository.save(schedule); // 업데이트된 schedule 저장
        }
    }

    public ScheduleResponseDto getScheduleByMentor(PrincipalDetails principalDetails) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();

        if (mentor == null) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Schedule> scheduleList = scheduleRepository.findAllByMentor(mentor);
        if (scheduleList.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR);
        }

        // 날짜별로 예약된 시간을 저장할 Map 생성
        Map<String, List<Integer>> dateToHoursMap = new HashMap<>();

        for (Schedule schedule : scheduleList) {
            // YYYY-MM-DD 형식으로 날짜 문자열 생성
            String dateStr = String.format("%d-%02d-%02d",
                    schedule.getLocalDate().getYear(),
                    schedule.getLocalDate().getMonthValue(),
                    schedule.getLocalDate().getDayOfMonth());

            List<Integer> reservedHours = new ArrayList<>();

            // 예약된 시간대 추출
            for (int hour = 0; hour < schedule.getHourSlots().size(); hour++) {
                if (schedule.getHourSlots().get(hour)) {
                    reservedHours.add(hour);
                }
            }

            // Map에 날짜와 예약된 시간 리스트 추가
            dateToHoursMap.put(dateStr, reservedHours);
        }

        return new ScheduleResponseDto(dateToHoursMap);
    }

    public ScheduleResponseDto getScheduleByMentorId(Long mentorId) {
        Mentor mentor = mentorService.getMentor(mentorId);

        if (mentor == null) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Schedule> scheduleList = scheduleRepository.findAllByMentor(mentor);
        if (scheduleList.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR);
        }

        // 날짜별로 예약된 시간을 저장할 Map 생성
        Map<String, List<Integer>> dateToHoursMap = new HashMap<>();

        for (Schedule schedule : scheduleList) {
            // YYYY-MM-DD 형식으로 날짜 문자열 생성
            String dateStr = String.format("%d-%02d-%02d",
                    schedule.getLocalDate().getYear(),
                    schedule.getLocalDate().getMonthValue(),
                    schedule.getLocalDate().getDayOfMonth());

            List<Integer> reservedHours = new ArrayList<>();

            // 예약된 시간대 추출
            for (int hour = 0; hour < schedule.getHourSlots().size(); hour++) {
                if (schedule.getHourSlots().get(hour)) {
                    reservedHours.add(hour);
                }
            }

            // Map에 날짜와 예약된 시간 리스트 추가
            dateToHoursMap.put(dateStr, reservedHours);
        }

        return new ScheduleResponseDto(dateToHoursMap);

    }

    // 스케줄 존재 여부 검증
    private Schedule validateScheduleExists(Mentor mentor, LocalDate localDate) {
        return scheduleRepository.findByMentorAndLocalDate(mentor, localDate)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR));
    }

    private void validateConsultationOwnership(Member mentor, Consultation consultation) {
        if (mentor.getMentor() == null) {
            throw new BusinessException(ErrorCode.MENTOR_NOT_REGISTERED);
        }
        if (consultation.isNotMentor(mentor.getMentor())) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
    }

    private void validateConsultationMember(Member member, Consultation consultation) {
        if (consultation.isNotMentee(member) && consultation.isNotMentor(member.getMentor()) ) {
            throw new BusinessException(ErrorCode.CONSULTATION_MEMBER_IS_NOT_PARTICIPANT);
        }
    }

    public Consultation validateConsultationExists(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONSULTATION_NOT_FOUND_ERROR));
    }

    private void validateMentee(Member member) {
        if (member.getMentor() != null) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_FOR_MENTOR);
        }
    }

    @Transactional
    public Page<Consultation> getConsultationPage(PrincipalDetails principalDetails,
                                                  ConsultationListRequestDto consultationListRequestDto) {
        PageRequest pageRequest = getConsultationPageRequest(consultationListRequestDto);
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        Page<Consultation> consultations = consultationRepository.findAllByMember(
                member, consultationListRequestDto.getConsultation_status(), pageRequest);


        return consultations;
    }

    private PageRequest getConsultationPageRequest(ConsultationListRequestDto consultationListRequestDto) {
        return PageRequest.of(
                consultationListRequestDto.getPage(),
                consultationListRequestDto.getSize(),
                Sort.by("id").descending()
        );
    }
}
