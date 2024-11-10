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

        // 기존 예방 설문 결과 존재 여부 유효성 검사
        if (preventionRepository.existsByMember(member)) {
            throw new BusinessException(ErrorCode.PREVENTION_ALREADY_EXISTS);
        }

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
    public PreventionResponseDto getPrevention(String memberEmail) {
        Member member = memberService.getMemberByEmail(memberEmail);

        Prevention prevention = preventionRepository.findByMember(member)
            .orElseThrow(() -> new BusinessException(ErrorCode.PREVENTION_NOT_FOUND));

        return new PreventionResponseDto(prevention);
    }
}
