package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.repository.MemberRepository;
import boomerang.progress.domain.MainStepEnum;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.dto.MainStepResponseDto;
import boomerang.progress.dto.ProgressDetailsResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.repository.ProgressRepository;
import boomerang.progress.util.ProgressStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProgressService {

    private final MemberRepository memberRepository;
    private final ProgressRepository progressRepository;


    //유저의 타입 검사
    public ProgressType checkUserType(PrincipalDetails principalDetails, ProgressTypeRequestDto progressTypeRequestDto) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());

        if (member.hasProgress()) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }

        ProgressType progressType = checkType(progressTypeRequestDto); //타입계산

        Progress savedProgress = progressRepository.save(new Progress(member, progressType));
        member.registerProgress(savedProgress);
        memberRepository.save(member);

        return progressType;
    }

    //유저의 타입 조회
    public ProgressType getUserType(PrincipalDetails principalDetails) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        return progress.getProgressType();
    }

    //유저의 진행도 전체 조회
    public ProgressDetailsResponseDto getProgressDetails(PrincipalDetails principalDetails) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        return new ProgressDetailsResponseDto(progress);
    }

    //특정 메인 단계만 조회
    public MainStepResponseDto getSubStepsByMainStep(PrincipalDetails principalDetails, MainStepEnum mainStepEnum) {
        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        return new MainStepResponseDto(progress.findMainStepByEnum(mainStepEnum));
    }

    //특정 서브단계만 조회
    public SubStepResponseDto getSubStepStatus(PrincipalDetails principalDetails,
                                               MainStepEnum mainStepEnum,
                                               SubStepEnum subStepEnum) {
        //메인단계와 서브단계가 적절한 매칭인지 검사
        if (!subStepEnum.isMatchingMainStep(mainStepEnum)) {
            throw new BusinessException(ErrorCode.PROGRESS_SUB_MAIN_DO_NOT_MATCH);
        }

        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        return progress.findSubStepByEnum(subStepEnum);
    }

    //진행도 업데이트
    public SubStepResponseDto completeProgress(PrincipalDetails principalDetails, MainStepEnum mainStepEnum, SubStepEnum subStepEnum) {
        if (!subStepEnum.isMatchingMainStep(mainStepEnum)) {
            throw new BusinessException(ErrorCode.PROGRESS_SUB_MAIN_DO_NOT_MATCH);
        }

        Member member = getMemberOrThrow(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        Progress updatedProgress = ProgressStrategy.updateProgress(progress, mainStepEnum, subStepEnum);
        progressRepository.save(updatedProgress);

//        return new SubStepResponseDto(updatedProgress);

        return null;
    }

    private Progress getProgressByMember(Member member) {
        Progress progress = member.getProgress();

        if (progress == null) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }
        return progress;
    }

    private Member getMemberOrThrow(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NON_EXISTENT));
    }

    private ProgressType checkType(ProgressTypeRequestDto progressTypeRequestDto) {
        Boolean isMemberInsureds = progressTypeRequestDto.getIsInsured();
        Boolean isMemberContractTerminated = progressTypeRequestDto.getIsContractTerminated();

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
