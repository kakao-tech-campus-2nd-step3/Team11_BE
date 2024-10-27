package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.oauth.dto.PrincipalDetails;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.progress.domain.*;
import boomerang.progress.dto.MainStepResponseDto;
import boomerang.progress.dto.ProgressDetailsResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.repository.ProgressRepository;
import boomerang.progress.util.ProgressStrategy;
import boomerang.progress.util.ProgressStrategyFactory;
import boomerang.progress.util.ProgressTypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProgressService {

    private final MemberService memberService;
    private final ProgressRepository progressRepository;

    //유저의 타입 검사
    @Transactional
    public ProgressType checkUserType(PrincipalDetails principalDetails, ProgressTypeRequestDto progressTypeRequestDto) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());

        if (member.hasProgress()) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }

        ProgressType progressType = ProgressTypeResolver.checkType(progressTypeRequestDto);

        ProgressStrategy progressStrategy = ProgressStrategyFactory.getStrategy(progressType);
        Progress savedProgress = progressRepository.save(progressStrategy.makeProgress(member));
        member.registerProgress(savedProgress);

        return progressType;
    }

    //유저의 타입 조회
    public ProgressType getUserType(PrincipalDetails principalDetails) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        return Optional.ofNullable(member.getProgressType())
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }

    //유저의 진행도 전체 조회
    @Transactional(readOnly = true)
    public ProgressDetailsResponseDto getProgressDetails(PrincipalDetails principalDetails) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        return new ProgressDetailsResponseDto(progress);
    }

    //특정 메인 단계만 조회
    @Transactional(readOnly = true)
    public MainStepResponseDto getSubStepsByMainStep(PrincipalDetails principalDetails, MainStepEnum mainStepEnum) {
        System.out.println("mainStepEnum = " + mainStepEnum.getMainStepName());
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);

        return new MainStepResponseDto(mainStep);
    }

    //특정 서브단계만 조회
    @Transactional(readOnly = true)
    public SubStepResponseDto getSubStepStatus(PrincipalDetails principalDetails,
                                               MainStepEnum mainStepEnum,
                                               SubStepEnum subStepEnum) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);
        SubStep subStep = getSubStepByEnum(mainStep, subStepEnum);

        return new SubStepResponseDto(subStep);
    }

    //진행도 업데이트
    @Transactional
    public SubStepResponseDto completeProgress(PrincipalDetails principalDetails, MainStepEnum mainStepEnum, SubStepEnum subStepEnum) {

        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);
        SubStep subStep = getSubStepByEnum(mainStep, subStepEnum);

        subStep.markAsComplete();
        return new SubStepResponseDto(subStep);
    }

    //진행도 완료 취소
    @Transactional
    public SubStepResponseDto revertProgressToIncomplete(PrincipalDetails principalDetails, MainStepEnum mainStepEnum, SubStepEnum subStepEnum) {
        Member member = memberService.getMemberByEmail(principalDetails.getMemberEmail());
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);
        SubStep subStep = getSubStepByEnum(mainStep, subStepEnum);

        subStep.markAsIncomplete();
        return new SubStepResponseDto(subStep);

    }


    private MainStep getMainStepByEnum(Progress progress, MainStepEnum mainStepEnum) {
        return progress.getMainStepByEnum(mainStepEnum)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_NOT_INCLUDED_MAIN));
    }

    private SubStep getSubStepByEnum(MainStep mainStep, SubStepEnum subStepEnum) {
        return mainStep.getSubStepByEnum(subStepEnum)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_NOT_INCLUDED_SUB));
    }

    private Progress getProgressByMember(Member member) {
        Progress progress = member.getProgress();
        if (progress == null) {
            throw new BusinessException(ErrorCode.PROGRESS_NON_EXISTENT);
        }
        return progress;
    }


}
