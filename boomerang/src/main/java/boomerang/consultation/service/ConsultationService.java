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
    public ScheduleResponseDto registerSchedule(PrincipalDetails principalDetails, ScheduleRequestDto scheduleRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();
        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Map<String, List<Integer>>> dayList = new ArrayList<>();
        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()){
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()){
                LocalDate date = LocalDate.of(2024, scheduleRequestDto.getMonth(),Integer.parseInt(entry.getKey())); // DTO의 월, 일로 LocalDate 생성
                Schedule schedule = scheduleRepository.findByMentorAndDate(mentor, date)
                        .orElse(new Schedule(mentor,date)); // 해당 날짜의 Schedule이 없으면 새로 생성
                List<Integer> hours = entry.getValue();
                List<Integer> reservedHours = new ArrayList<>();
                for (int hour : hours) {
                    if (hour < 0 || hour >= 24) {
                        throw new BusinessException(ErrorCode.CONSULTATION_TIME_REQUEST_ERROR); // 0 ~ 23 범위에 벗어나는 시간 예외 처리
                    }
                    if (schedule.getHourlySlots().get(hour)) {
                        throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS); // 중복된 시간 예외 처리
                    }
                    schedule.reserveSlot(hour); // 시간대 예약
                    reservedHours.add(hour);
                }
                scheduleRepository.save(schedule);
                Map<String, List<Integer>> dayEntry = new HashMap<>();
                dayEntry.put(entry.getKey(), reservedHours);
                dayList.add(dayEntry);
            }
        }
        return new ScheduleResponseDto(scheduleRequestDto.getMonth(), dayList);
    }

    @Transactional
    public void deleteSchedule(PrincipalDetails principalDetails, ScheduleRequestDto scheduleRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();
        if (!mentor.getMember().equals(member)) {
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        for (Map<String, List<Integer>> dateEntry : scheduleRequestDto.getDayList()){
            for (Map.Entry<String, List<Integer>> entry : dateEntry.entrySet()){
                LocalDate date = LocalDate.of(2024, scheduleRequestDto.getMonth(),Integer.parseInt(entry.getKey()));
                Schedule schedule = scheduleRepository.findByMentorAndDate(mentor, date)
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

    public ScheduleResponseListDto getScheduleByMentor(PrincipalDetails principalDetails){
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Mentor mentor = member.getMentor();
        if (member.getMentor() == null){
            throw new BusinessException(ErrorCode.CONSULTATION_NOT_A_MENTOR);
        }
        List<Schedule> scheduleList = scheduleRepository.findAllByMentor(mentor);
        if (scheduleList.isEmpty()) {
            throw new BusinessException(ErrorCode.SCHEDULE_NOT_FOUND_ERROR);
        }

        // 월별로 일정을 정리할 맵 생성
        Map<Integer, List<Map<String, List<Integer>>>> monthToDaysMap = new HashMap<>();

        for (Schedule schedule : scheduleList) {
            int month = schedule.getDate().getMonthValue();
            String day = String.valueOf(schedule.getDate().getDayOfMonth());
            List<Integer> reservedHours = new ArrayList<>();

            // 예약된 시간대만 추출하여 리스트에 추가
            for (int hour = 0; hour < schedule.getHourlySlots().size(); hour++) {
                if (schedule.getHourlySlots().get(hour)) {
                    reservedHours.add(hour);
                }
            }

            // 해당 월이 이미 존재하면 해당 일 데이터 추가, 없으면 새로 생성
            monthToDaysMap.computeIfAbsent(month, k -> new ArrayList<>())
                    .add(Collections.singletonMap(day, reservedHours));
        }

        // Map을 ScheduleMonthDto 리스트로 변환
        List<ScheduleMonthDto> scheduleMonthDtoList = monthToDaysMap.entrySet().stream()
                .map(entry -> new ScheduleMonthDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new ScheduleResponseListDto(scheduleMonthDtoList);
    }

}
