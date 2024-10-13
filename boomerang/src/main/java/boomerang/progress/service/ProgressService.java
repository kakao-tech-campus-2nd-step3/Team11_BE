package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.repository.MemberRepository;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.dto.ProgressTypeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgressService {
    private final MemberRepository memberRepository;

    public ProgressType checkUserType(PrincipalDetails principalDetails, ProgressTypeRequestDto progressTypeRequestDto) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());

        if (member.hasProgressType()) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }

        ProgressType progressType = checkType(progressTypeRequestDto.getIsInsured(), progressTypeRequestDto.getIsContractTerminated());

        //멤버 엔티티에 피해 유형 등록
        member.registerProgressType(progressType);

        memberRepository.save(member);
        return progressType;
    }

    private Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }

    private ProgressType checkType(Boolean isMemberInsureds, Boolean isMemberContractTerminated) {
        if (isMemberInsureds && isMemberContractTerminated) {
            return ProgressType.D;
        }
        if (!isMemberInsureds && isMemberContractTerminated) {
            return ProgressType.C;
        }
        if (isMemberInsureds && !isMemberContractTerminated) {
            return ProgressType.B;
        }
        if (!isMemberInsureds && !isMemberContractTerminated) {
            return ProgressType.A;
        }
        throw new BusinessException(ErrorCode.PROGRESS_TYPE_REQUEST_ERROR);
    }
}
