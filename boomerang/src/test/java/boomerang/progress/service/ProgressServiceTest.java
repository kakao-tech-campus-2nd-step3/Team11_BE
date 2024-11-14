package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.service.MemberService;
import boomerang.progress.domain.*;
import boomerang.progress.dto.ProgressByMainResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.repository.ProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private SubStepInfoService subStepInfoService;

    @InjectMocks
    private ProgressService progressService;

    private Member member;
    private Progress progress;
    private MainStep mainStep;
    private SubStep subStep;
    private String testEmail;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        member = new Member(new MemberServiceDto(testEmail, "nickname"));

        progress = new Progress(member, ProgressType.A);

        mainStep = new MainStep(MainStepEnum.A1, progress);

        subStep = new SubStep(mainStep, SubStepEnum.A1_1);

        mainStep.registerSubStepList(List.of(subStep));

        progress.registerMainStepList(List.of(mainStep));

        member.registerProgress(progress);
    }

    @Test
    void testCheckUserType() {
        // given
        ProgressTypeRequestDto requestDto = new ProgressTypeRequestDto(true, LeaseTypeEnum.JEONSE);
        Member memberWithoutProgress = new Member(new MemberServiceDto(testEmail, "nickname"));
        given(memberService.getMemberByEmail(testEmail)).willReturn(memberWithoutProgress);
        given(progressRepository.save(any(Progress.class))).willReturn(progress);

        // when
        ProgressType result = progressService.checkUserType(testEmail, requestDto);

        // then
        assertThat(result).isEqualTo(ProgressType.A);
        then(progressRepository).should(times(1)).save(any(Progress.class));
    }

    @Test
    void testCheckUserType_AlreadyExists() {
        // given
        ProgressTypeRequestDto requestDto = new ProgressTypeRequestDto(true, LeaseTypeEnum.JEONSE);
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);

        // when/then
        assertThatThrownBy(() -> progressService.checkUserType(testEmail, requestDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.PROGRESS_TYPE_EXISTS.getMessage());
    }

    @Test
    void testGetUserType() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);

        // when
        ProgressType result = progressService.getUserType(testEmail);

        // then
        assertThat(result).isEqualTo(ProgressType.A);
    }

    @Test
    void testGetProgressDetails() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(subStepInfoService.getSubStepList(any()))
                .willReturn(List.of(new SubStepResponseDto(subStep, "test content")));

        // when
        ProgressByMainResponseDto result = progressService.getProgressDetails(testEmail);

        // then
        assertThat(result).isNotNull();
        then(subStepInfoService).should(times(1)).getSubStepList(any());
    }

    @Test
    void testGetSubStepsByMainStep() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(subStepInfoService.getSubStepList(any()))
                .willReturn(List.of(new SubStepResponseDto(subStep, "test content")));

        // when
        ProgressByMainResponseDto result = progressService.getSubStepsByMainStep(testEmail, MainStepEnum.A1);

        // then
        assertThat(result).isNotNull();
        then(subStepInfoService).should(times(1)).getSubStepList(any());
    }

    @Test
    void testCompleteProgress() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(subStepInfoService.getSubStepContent(any())).willReturn("content");

        // when
        SubStepResponseDto result = progressService.completeProgress(testEmail, MainStepEnum.A1, SubStepEnum.A1_1);

        // then
        assertThat(result).isNotNull();
        assertThat(subStep.isCompletion()).isTrue();
    }

    @Test
    void testRevertProgressToIncomplete_AlreadyIncomplete() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);

        // when/then
        assertThatThrownBy(() -> progressService.revertProgressToIncomplete(testEmail, MainStepEnum.A1, SubStepEnum.A1_1))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.PROGRESS_ALREADY_INCOMPLETE.getMessage());
    }

    @Test
    void testGetCurrentMainStep() {
        // when
        MainStep result = progressService.getCurrentMainStep(progress);

        // then
        assertThat(result).isEqualTo(mainStep);
    }
}