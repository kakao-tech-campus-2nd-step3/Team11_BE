package boomerang.consultation.service;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        Consultation consultation = validateConsultationExists(consultationId);


    }

        Consultation consultation = validateConsultationExists(consultationId);


        consultation.complete();
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

        List<ScheduleDayDto> dayList = new ArrayList<>();
        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()){
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()){
                LocalDate date = LocalDate.of(LocalDate.now().getYear(), scheduleRequestDto.getMonth(),Integer.parseInt(entry.getKey())); // DTO의 월, 일로 LocalDate 생성
                Schedule schedule = scheduleRepository.findByMentorAndLocalDate(mentor, date)
                        .orElse(new Schedule(mentor,date)); // 해당 날짜의 Schedule이 없으면 새로 생성
                List<Integer> hours = entry.getValue();
                List<ScheduleHourDto> reservedHours = new ArrayList<>();
                for (int hour : hours) {
                    checkHour(hour, hours); // 시간 범위 검증 로직 별도 메서드로 분리
                    if (schedule.getHourSlots().get(hour)) {
                        throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS); // 중복된 시간 예외 처리
                    }
                    schedule.reserveHourSlot(hour); // 시간대 예약
                    reservedHours.add(new ScheduleHourDto(hour));
                }
                scheduleRepository.save(schedule);
                ScheduleDayDto dayEntry = new ScheduleDayDto(entry.getKey(), reservedHours);
                dayList.add(dayEntry);
            }
        }
        return new ScheduleResponseDto(scheduleRequestDto.getMonth(), dayList);
    }

    @Transactional
    public void deleteSchedule(PrincipalDetails principalDetails,
        ScheduleRequestDto scheduleRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();
        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()){
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()){
                LocalDate date = LocalDate.of(LocalDate.now().getYear(), scheduleRequestDto.getMonth(),Integer.parseInt(entry.getKey()));
                Schedule schedule = scheduleRepository.findByMentorAndLocalDate(mentor, date)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR));
                List<Integer> hours = entry.getValue();
                for (int hour : hours) {
                    checkHour(hour, hours);
                    schedule.unreserveHourSlot(hour);
                    scheduleRepository.save(schedule);
                }
            }
        }
    }

    public ScheduleResponseListDto getScheduleByMentor(PrincipalDetails principalDetails) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();

        if (mentor == null) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Schedule> scheduleList = scheduleRepository.findAllByMentor(mentor);
        if (scheduleList.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR);
        }

        // 월별로 일정을 정리할 맵 생성
        Map<Integer, List<ScheduleDayDto>> monthToDaysMap = new HashMap<>();

        for (Schedule schedule : scheduleList) {
            int month = schedule.getLocalDate().getMonthValue();
            String day = String.valueOf(schedule.getLocalDate().getDayOfMonth());
            List<ScheduleHourDto> reservedHours = new ArrayList<>();

            // 예약된 시간대만 추출하여 HourSchedule로 추가
            for (int hour = 0; hour < schedule.getHourSlots().size(); hour++) {
                if (schedule.getHourSlots().get(hour)) {
                    reservedHours.add(new ScheduleHourDto(hour));
                }
            }

            // DaySchedule 생성
            ScheduleDayDto daySchedule = new ScheduleDayDto(day, reservedHours);

            // 해당 월이 이미 존재하면 일 데이터 추가, 없으면 새로 생성
            monthToDaysMap.computeIfAbsent(month, k -> new ArrayList<>()).add(daySchedule);
        }

        // Map을 ScheduleMonthDto 리스트로 변환
        List<ScheduleMonthDto> scheduleMonthDtoList = monthToDaysMap.entrySet().stream()
            .map(entry -> new ScheduleMonthDto(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

        return new ScheduleResponseListDto(scheduleMonthDtoList);
    }

    public ScheduleResponseListDto getScheduleByMentorId(Long mentorId) {
        Mentor mentor = mentorService.getMentor(mentorId);

        if (mentor == null) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Schedule> scheduleList = scheduleRepository.findAllByMentor(mentor);
        if (scheduleList.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR);
        }

        // 월별로 일정을 정리할 맵 생성
        Map<Integer, List<ScheduleDayDto>> monthToDaysMap = new HashMap<>();

        for (Schedule schedule : scheduleList) {
            int month = schedule.getLocalDate().getMonthValue();
            String day = String.valueOf(schedule.getLocalDate().getDayOfMonth());
            List<ScheduleHourDto> reservedHours = new ArrayList<>();

            // 예약된 시간대만 추출하여 HourSchedule로 추가
            for (int hour = 0; hour < schedule.getHourSlots().size(); hour++) {
                if (schedule.getHourSlots().get(hour)) {
                    reservedHours.add(new ScheduleHourDto(hour));
                }
            }

            // DaySchedule 생성
            ScheduleDayDto daySchedule = new ScheduleDayDto(day, reservedHours);

            // 해당 월이 이미 존재하면 일 데이터 추가, 없으면 새로 생성
            monthToDaysMap.computeIfAbsent(month, k -> new ArrayList<>()).add(daySchedule);
        }

        // Map을 ScheduleMonthDto 리스트로 변환
        List<ScheduleMonthDto> scheduleMonthDtoList = monthToDaysMap.entrySet().stream()
                .map(entry -> new ScheduleMonthDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ScheduleResponseListDto(scheduleMonthDtoList);

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

        // 현재 시간을 가져와 조회된 consultations의 상태를 확인 후 업데이트
        LocalDateTime now = LocalDateTime.now();
        consultations.forEach(consultation -> {
            if (consultation.getConsultationDateTime().isBefore(now)
                    && consultation.getConsultationStatus() == ConsultationStatus.RECEIVED) {
                consultation.complete(); // 상태를 FINISHED으로 변경, 상담 시간이 되어도 확정되지 않으면 확정전 -> 상담완료
            }
            if (consultation.getConsultationDateTime().isBefore(now)
                    && consultation.getConsultationStatus() == ConsultationStatus.PENDING) {
                consultation.start(); // 상태를 ONGOING으로 변경, 확정된 상담이 신청 시간이 지나면 진행전 -> 진행중
            }
            if (consultation.getConsultationDateTime().plusHours(1).plusMinutes(30).isBefore(now)
                    && consultation.getConsultationStatus() == ConsultationStatus.ONGOING) {
                consultation.complete(); // 상태를 FINISHED으로 변경, 진행중인 상담이 상담시간보다 1시간 30분이 지나면 진행중 -> 상담완료
            }
        });

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
