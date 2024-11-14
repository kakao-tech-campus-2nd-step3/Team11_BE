package boomerang.prevention.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.dto.MemberServiceDto;
import boomerang.member.service.MemberService;
import boomerang.prevention.domain.Prevention;
import boomerang.prevention.dto.MortgageRequestDto;
import boomerang.prevention.dto.PreventionRequestDto;
import boomerang.prevention.dto.PreventionResponseDto;
import boomerang.prevention.repository.PreventionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PreventionServiceTest {

    @Mock
    private PreventionRepository preventionRepository;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private PreventionService preventionService;

    private Member member;
    private Prevention prevention;
    private PreventionRequestDto requestDto;
    private String testEmail;
    private String testAddress;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testAddress = "test address";

        member = new Member(new MemberServiceDto(testEmail, "nickname"));

        List<MortgageRequestDto> mortgages = Collections.singletonList(
                new MortgageRequestDto(1000000L, "creditor", LocalDate.now())
        );

        requestDto = new PreventionRequestDto(
                testAddress,        // address
                2000000L,          // housePrice
                500000L,           // depositAmount
                mortgages          // mortgages
        );

        prevention = new Prevention(member, requestDto);
    }

    @Test
    void testSavePrevention() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(preventionRepository.save(any(Prevention.class))).willReturn(prevention);

        // when
        PreventionResponseDto result = preventionService.savePrevention(testEmail, requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(testAddress);
        then(preventionRepository).should(times(1)).save(any(Prevention.class));
    }

    @Test
    void testGetPreventions() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(preventionRepository.findAllByMemberOrderByIdDesc(member))
                .willReturn(Collections.singletonList(prevention));

        // when
        List<PreventionResponseDto> results = preventionService.getPreventions(testEmail);

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAddress()).isEqualTo(testAddress);
        then(preventionRepository).should(times(1)).findAllByMemberOrderByIdDesc(member);
    }

    @Test
    void testGetPreventions_NotFound() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(preventionRepository.findAllByMemberOrderByIdDesc(member))
                .willReturn(Collections.emptyList());

        // when/then
        assertThatThrownBy(() -> preventionService.getPreventions(testEmail))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.PREVENTION_NOT_FOUND.getMessage());
    }

    @Test
    void testGetPreventionsByAddress() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(preventionRepository.findTopByMemberAndAddressOrderByIdDesc(member, testAddress))
                .willReturn(Optional.of(prevention));

        // when
        PreventionResponseDto result = preventionService.getPreventionsByAddress(testEmail, testAddress);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAddress()).isEqualTo(testAddress);
        then(preventionRepository).should(times(1))
                .findTopByMemberAndAddressOrderByIdDesc(member, testAddress);
    }

    @Test
    void testGetPreventionsByAddress_NotFound() {
        // given
        given(memberService.getMemberByEmail(testEmail)).willReturn(member);
        given(preventionRepository.findTopByMemberAndAddressOrderByIdDesc(member, testAddress))
                .willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> preventionService.getPreventionsByAddress(testEmail, testAddress))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.PREVENTION_NOT_FOUND.getMessage());
    }
}