package boomerang.prevention.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.prevention.domain.Prevention;
import boomerang.prevention.dto.MortgageRequestDto;
import boomerang.prevention.dto.PreventionRequestDto;
import boomerang.prevention.dto.PreventionResponseDto;
import boomerang.prevention.repository.PreventionRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PreventionService {

    private final PreventionRepository preventionRepository;
    private final MemberService memberService;

    @Transactional
    public PreventionResponseDto savePrevention(String memberEmail,
        PreventionRequestDto requestDto) {
        Member member = memberService.getMemberByEmail(memberEmail);

        Prevention prevention = new Prevention(member, requestDto);

        if (requestDto.getMortgages() != null) {
            for (MortgageRequestDto mortgageDto : requestDto.getMortgages()) {
                prevention.addMortgage(
                    mortgageDto.getAmount(),
                    mortgageDto.getCreditor(),
                    mortgageDto.getRegistrationDate()
                );
            }
        }

        Prevention savedPrevention = preventionRepository.save(prevention);
        return new PreventionResponseDto(savedPrevention);
    }

    @Transactional(readOnly = true)
    public List<PreventionResponseDto> getPreventions(String memberEmail) {
        Member member = memberService.getMemberByEmail(memberEmail);

        List<Prevention> preventions = preventionRepository.findAllByMemberOrderByIdDesc(member);

        if (preventions.isEmpty()) {
            throw new BusinessException(ErrorCode.PREVENTION_NOT_FOUND);
        }

        return preventions.stream()
            .map(PreventionResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PreventionResponseDto getPreventionsByAddress(String memberEmail, String address) {
        Member member = memberService.getMemberByEmail(memberEmail);
        Prevention prevention = preventionRepository.findTopByMemberAndAddressOrderByIdDesc(member, address)
            .orElseThrow(() -> new BusinessException(ErrorCode.PREVENTION_NOT_FOUND));

        return new PreventionResponseDto(prevention);
    }
}
