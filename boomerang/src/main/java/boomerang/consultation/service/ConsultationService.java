package boomerang.consultation.service;

import boomerang.consultation.domain.*;
import boomerang.consultation.dto.*;
import boomerang.consultation.repository.*;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.repository.MentorRepository;
import boomerang.mentor.service.MentorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final MonthScheduleRepository monthScheduleRepository;
    private final DayScheduleRepository dayScheduleRepository;
    private final TimeScheduleRepository timeScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberService memberService;
    private final MentorService mentorService;

    public ConsultationResponseDto requestConsultation(PrincipalDetails principalDetails, ConsultationRequestDto consultationRequestDto) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = mentorService.getMentor(consultationRequestDto.getMentorId());
        LocalDate localDate = LocalDate.now();

        if (consultationRepository.existsByMenteeAndMentorAndConsultationDate(mentee, mentor, localDate)) {
            throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS);
        }


        Consultation savedConsultation = consultationRepository.save(new Consultation(mentee, mentor, localDate));

        return new ConsultationResponseDto(savedConsultation);
    }

    public ConsultationResponseDto completeConsultation(PrincipalDetails principalDetails, Long consultationId) {
        Member mentee = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Consultation consultation = getConsultation(consultationId);

        if (!consultation.isMentee(mentee)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTEE);
        }

        if (consultation.isFinished()) {
            throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_FINISHED);
        }

        consultation.complete();

        Consultation savedConsultation = consultationRepository.save(consultation);
        return new ConsultationResponseDto(savedConsultation);
    }

    public ConsultationResponseDto getConsultationDetail(Long consultationId) {
        Consultation consultation = getConsultation(consultationId);
        return new ConsultationResponseDto(consultation);
    }


    public ConsultationResponseListDto getConsultationOfUser(PrincipalDetails principalDetails, Pageable pageable) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        Page<ConsultationResponseDto> consultationResponsePage = consultationRepository.findAllByMentee(member, pageable)
                .map(ConsultationResponseDto::new);

        return new ConsultationResponseListDto(consultationResponsePage);
    }

    public ConsultationResponseListDto getConsultationOfMentor(Long mentorId, Pageable pageable) {
        Mentor mentor = mentorService.getMentor(mentorId);
        Page<ConsultationResponseDto> consultationResponsePage = consultationRepository.findAllByMentor(mentor, pageable)
                .map(ConsultationResponseDto::new);

        return new ConsultationResponseListDto(consultationResponsePage);

    }

    public Consultation getConsultation(long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CONSULTATION_NOT_FOUND_ERROR));
    }

    @Transactional
    public void registerSchedule(PrincipalDetails principalDetails, ScheduleRequestDto scheduleRequestDto) {
        MonthSchedule monthSchedule = new MonthSchedule(scheduleRequestDto.getMonth());
        List<DaySchedule> dayScheduleList = new ArrayList<>();

        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()) {
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()) {
                DaySchedule daySchedule = new DaySchedule(Integer.parseInt(entry.getKey()),monthSchedule);

                List<TimeSchedule> timeSlots = entry.getValue().stream()
                        .map(hour -> {
                            TimeSchedule timeSlot = new TimeSchedule(hour, daySchedule);
                            return timeSlot;
                        }).collect(Collectors.toList());

                daySchedule.setTimeScheduless(timeSlots);
                System.out.println(daySchedule);
                dayScheduleRepository.save(daySchedule);
                dayScheduleList.add(daySchedule);
            }
        }

        monthSchedule.setDaySchedules(dayScheduleList);

        monthScheduleRepository.save(monthSchedule);
    }

    @Transactional
    public ScheduleResponseDto registerSchedule2(PrincipalDetails principalDetails, ScheduleRequestDto scheduleRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = mentorService.getMentor(scheduleRequestDto.getMentorId());
        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Schedule> scheduleList = new ArrayList<>();
        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()){
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()){
                LocalDate date = LocalDate.of(2024, scheduleRequestDto.getMonth(),Integer.parseInt(entry.getKey())); // DTO의 월, 일로 LocalDate 생성
                Schedule schedule = scheduleRepository.findByDate(date)
                        .orElse(new Schedule(mentor,date)); // 해당 날짜의 Schedule이 없으면 새로 생성
                List<Integer> hours = entry.getValue();
                for (int hour : hours) {
                    if (hour < 0 || hour >= 24) {
                        throw new BusinessException(ErrorCode.CONSULTATION_TIME_REQUEST_ERROR); // 0 ~ 23 범위에 벗어나는 시간 예외 처리
                    }
                    if (schedule.getHourlySlots().get(hour)) {
                        throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS); // 중복된 시간 예외 처리
                    }
                    schedule.reserveSlot(hour); // 시간대 예약
                    scheduleRepository.save(schedule);
                    if (schedule.getHourlySlots().get(hour) == Boolean.TRUE) {
                        scheduleList.add(schedule);
                    }
                }
            }
        }
        return new ScheduleResponseDto(scheduleList);
    }

    @Transactional
    public void deleteSchedule(PrincipalDetails principalDetails, ScheduleRequestDto scheduleRequestDto) {
        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()){
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()){
                LocalDate date = LocalDate.of(2024, scheduleRequestDto.getMonth(),Integer.parseInt(entry.getKey()));
                Schedule schedule = scheduleRepository.findByDate(date)
                        .orElseThrow(() -> new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR));
                List<Integer> hours = entry.getValue();
                for (int hour : hours) {
                    if (hour < 0 || hour >= 24) {
                        throw new BusinessException(ErrorCode.CONSULTATION_TIME_REQUEST_ERROR);
                    }
                    schedule.unreserveSlot(hour);
                    scheduleRepository.save(schedule);
                }
            }
        }
    }




}
