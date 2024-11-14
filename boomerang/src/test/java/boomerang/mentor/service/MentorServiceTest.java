package boomerang.mentor.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.PageResponseDto;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.service.MemberService;
import boomerang.mentor.domain.Mentor;
import boomerang.mentor.domain.MentorType;
import boomerang.mentor.dto.*;
import boomerang.mentor.repository.MentorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MentorServiceTest {

    @Mock
    private MentorRepository mentorRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MentorService mentorService;

    private Member member;
    private Mentor mentor;
    private MentorCreateRequestDto createRequestDto;

    @BeforeEach
    void setUp() {
        // Create MemberServiceDto
        MemberServiceDto memberServiceDto = new MemberServiceDto("test@example.com", "testNickname");

        member = new Member(memberServiceDto);
        member.verifyEmail();

        createRequestDto = new MentorCreateRequestDto(
                MentorType.LAWYER,
                "5years",
                "introduce",
                true,
                "contact"
        );

        mentor = new Mentor(
                createRequestDto.getMentorType(),
                createRequestDto.getCareer(),
                createRequestDto.getIntroduce(),
                createRequestDto.getAdvertisementStatus(),
                member,
                createRequestDto.getContact()
        );
    }

    @Test
    void testGetAllMentors() {
        // given
        MentorListRequestDto requestDto = new MentorListRequestDto();
        requestDto.setPage(0);
        requestDto.setSize(10);
        Page<Mentor> mockPage = new PageImpl<>(List.of(mentor));

        given(mentorRepository.findAllByIsDeletedFalse(any(PageRequest.class))).willReturn(mockPage);

        // when
        PageResponseDto<MentorResponseDto> result = mentorService.getAllMentors(requestDto);

        // then
        assertThat(result.getContent()).hasSize(1);
        then(mentorRepository).should(times(1)).findAllByIsDeletedFalse(any(PageRequest.class));
    }

    @Test
    void testGetMentorProfile() {
        // given
        Long mentorId = 1L;
        given(mentorRepository.findByIdAndIsDeletedFalse(mentorId)).willReturn(Optional.of(mentor));

        // when
        MentorResponseDto result = mentorService.getMentorProfile(mentorId);

        // then
        assertThat(result).isNotNull();
        then(mentorRepository).should(times(1)).findByIdAndIsDeletedFalse(mentorId);
    }

    @Test
    void testCreateMentor_NewMentor() {
        // given
        String email = "test@example.com";
        given(memberService.getMemberByEmail(email)).willReturn(member);
        given(mentorRepository.findByMember(member)).willReturn(Optional.empty());
        given(mentorRepository.save(any(Mentor.class))).willReturn(mentor);

        // when
        MentorResponseDto result = mentorService.createMentor(email, createRequestDto);

        // then
        assertThat(result).isNotNull();
        then(mentorRepository).should(times(1)).save(any(Mentor.class));
    }

    @Test
    void testUpdateMentor() {
        // given
        String email = "test@example.com";
        MentorUpdateRequestDto updateRequestDto = new MentorUpdateRequestDto(
                MentorType.LAWYER,
                "updated career",
                "updated intro",
                true,
                "updated contact"
        );

        given(memberService.getMemberByEmail(email)).willReturn(member);
        given(mentorRepository.findByMemberAndIsDeletedFalse(member)).willReturn(Optional.of(mentor));

        // when
        MentorResponseDto result = mentorService.updateMentor(email, updateRequestDto);

        // then
        assertThat(result).isNotNull();
        then(mentorRepository).should(times(1)).findByMemberAndIsDeletedFalse(member);
    }

    @Test
    void testDeleteMentor() {
        // given
        String email = "test@example.com";
        given(memberService.getMemberByEmail(email)).willReturn(member);
        given(mentorRepository.findByMemberAndIsDeletedFalse(member)).willReturn(Optional.of(mentor));

        // when
        mentorService.deleteMentor(email);

        // then
        assertThat(mentor.getIsDeleted()).isTrue();
        then(mentorRepository).should(times(1)).findByMemberAndIsDeletedFalse(member);
    }

    @Test
    void testGetMentor() {
        // given
        Long mentorId = 1L;
        given(mentorRepository.findByIdAndIsDeletedFalse(mentorId)).willReturn(Optional.of(mentor));

        // when
        Mentor result = mentorService.getMentor(mentorId);

        // then
        assertThat(result).isEqualTo(mentor);
        then(mentorRepository).should(times(1)).findByIdAndIsDeletedFalse(mentorId);
    }

    @Test
    void testGetMentor_NotFound() {
        // given
        Long mentorId = 1L;
        given(mentorRepository.findByIdAndIsDeletedFalse(mentorId)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> mentorService.getMentor(mentorId))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.MENTOR_NOT_FOUND.getMessage());
    }
}