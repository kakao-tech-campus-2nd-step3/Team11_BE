package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.repository.MemberRepository;
import boomerang.progress.domain.MainStep;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.domain.SubStep;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.repository.ProgressRepository;
import boomerang.progress.util.ProgressStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final MemberRepository memberRepository;
    private final ProgressRepository progressRepository;


    public ProgressType checkUserType(PrincipalDetails principalDetails, ProgressTypeRequestDto progressTypeRequestDto) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());

        if (member.hasProgressType()) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }

        ProgressType progressType = checkType(progressTypeRequestDto.getIsInsured(), progressTypeRequestDto.getIsContractTerminated());
        Progress progress = progressRepository.save(new Progress(member));

        //멤버 엔티티에 피해 유형 등록
        member.registerProgressType(progressType);

        //멤버 엔티티에 진행도 저장
        member.registerProgress(progress);

        memberRepository.save(member);

        return progressType;
    }

    //진행도 업데이트
    public SubStepResponseDto completeProgress(PrincipalDetails principalDetails, MainStep mainStep, SubStep subStep) {

        if (!subStep.isMatchingMainStep(mainStep)) {
            throw new BusinessException(ErrorCode.PROGRESS_SUB_MAIN_DO_NOT_MATCH);
        }

        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        Progress progress = Optional.ofNullable(member.getProgress())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
        //타입검사도 필요함.. ㅜㅜ

        Progress updatedProgress = ProgressStrategy.updateProgress(progress, mainStep, subStep);
        progressRepository.save(updatedProgress);

        return new SubStepResponseDto(updatedProgress);

    }


    public ProgressType getUserType(PrincipalDetails principalDetails) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        return Optional.ofNullable(member.getProgressType())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
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
