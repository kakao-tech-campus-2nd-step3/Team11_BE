package boomerang.consultation.service;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.domain.Schedule;
import boomerang.consultation.dto.ConsultationRequestDto;
import boomerang.consultation.dto.ScheduleRequestDto;
import boomerang.consultation.repository.ConsultationRepository;
import boomerang.consultation.repository.ScheduleRepository;
import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.domain.MentorType;
import boomerang.mentor.service.MentorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private MentorService mentorService;

    @InjectMocks
    private ConsultationService consultationService;

    private Member member;
    private Member mentorMember;
    private Mentor mentor;
    private ConsultationRequestDto consultationRequestDto;
    private PrincipalDetails principalDetails;
    private Schedule schedule;
    private Consultation consultation;
    private LocalDateTime consultationDateTime;

    @BeforeEach
    void setUp() {
        member = new Member(new MemberServiceDto("user@example.com", "nickname"));
        mentorMember = new Member(new MemberServiceDto("mentor@example.com", "mentorNickname"));
        mentor = new Mentor(MentorType.LAWYER, "5년 경력", "소개글", true, mentorMember, "contact@email.com");
        mentorMember.registerMentor(mentor);
        principalDetails = new PrincipalDetails(member);

        consultationDateTime = LocalDateTime.now().plusDays(1);
        consultationRequestDto = new ConsultationRequestDto(1L, consultationDateTime, "Test Title", "Test Content");
        schedule = new Schedule(mentor, consultationDateTime.toLocalDate());
        schedule.reserveHourSlot(consultationDateTime.getHour());
        consultation = new Consultation(member, mentor, consultationDateTime, "Test Title", "Test Content");
        consultation.confirm();
    }

    @Test
    void testRequestConsultation() {
        // given
        given(memberService.getMemberByEmail(principalDetails.getMemberEmail())).willReturn(member);
        given(mentorService.getMentor(any())).willReturn(mentor);
        given(scheduleRepository.findByMentorAndLocalDate(any(), any())).willReturn(Optional.of(schedule));
        given(consultationRepository.save(any())).willReturn(consultation);

        // when
        var result = consultationService.requestConsultation(principalDetails, consultationRequestDto);

        // then
        assertThat(result).isNotNull();
        then(consultationRepository).should(times(1)).save(any());
        then(scheduleRepository).should(times(1)).save(any());
    }

    @Test
    void testConfirmConsultation() {
        // given
        consultation.setStatus(ConsultationStatus.RECEIVED);
        given(consultationRepository.findById(any())).willReturn(Optional.of(consultation));
        given(memberService.getMemberByEmail(any())).willReturn(mentorMember);

        // when
        var result = consultationService.confirmConsultation(principalDetails, 1L);

        // then
        assertThat(result).isNotNull();
        then(consultationRepository).should(times(1)).save(any());
    }

    @Test
    void testRegisterSchedule() {
        // given
        PrincipalDetails mentorPrincipal = new PrincipalDetails(mentorMember);
        Map<String, List<Integer>> scheduleMap = new HashMap<>();
        scheduleMap.put(LocalDate.now().toString(), List.of(9, 10, 11));
        ScheduleRequestDto scheduleRequestDto = new ScheduleRequestDto(scheduleMap);

        given(memberService.getMemberByEmail(any())).willReturn(mentorMember);
        given(scheduleRepository.findByMentorAndLocalDate(any(), any())).willReturn(Optional.empty());

        // when
        var result = consultationService.registerSchedule(mentorPrincipal, scheduleRequestDto);

        // then
        assertThat(result).isNotNull();
        then(scheduleRepository).should(times(1)).save(any());
    }

    @Test
    void testFinishConsultation() {
        // given
        consultation.setStatus(ConsultationStatus.ONGOING);
        given(consultationRepository.findById(any())).willReturn(Optional.of(consultation));
        given(memberService.getMemberByEmail(any())).willReturn(member);

        // when
        consultationService.finishConsultation(principalDetails, 1L);

        // then
        then(consultationRepository).should(times(1)).save(any());
    }

    @Test
    void testDeleteConsultation_NotFound() {
        // given
        given(consultationRepository.findById(any())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> consultationService.deleteConsultation(principalDetails, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.CONSULTATION_NOT_FOUND_ERROR.getMessage());
    }
}