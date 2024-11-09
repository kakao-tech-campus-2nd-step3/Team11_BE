package boomerang.progress.service;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.member.service.MemberService;
import boomerang.progress.domain.MainStep;
import boomerang.progress.domain.MainStepEnum;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.domain.SubStep;
import boomerang.progress.domain.SubStepEnum;
import boomerang.progress.dto.ProgressByMainResponseDto;
import boomerang.progress.dto.ProgressTypeRequestDto;
import boomerang.progress.dto.SubStepResponseDto;
import boomerang.progress.repository.ProgressRepository;
import boomerang.progress.util.ProgressTypeResolver;
import boomerang.progress.util.ProgressUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final MemberService memberService;
    private final ProgressRepository progressRepository;
    private final SubStepInfoService subStepInfoService;

    //유저의 타입 검사
    @Transactional
    public ProgressType checkUserType(String email, ProgressTypeRequestDto progressTypeRequestDto) {
        Member member = memberService.getMemberByEmail(email);

        if (member.hasProgress()) {
            throw new BusinessException(ErrorCode.PROGRESS_TYPE_EXISTS);
        }

        ProgressType progressType = ProgressTypeResolver.checkType(progressTypeRequestDto);

        Progress savedProgress = progressRepository.save(
            ProgressUtil.makeProgress(progressType, member));
        member.registerProgress(savedProgress);

        return progressType;
    }

    //유저의 타입 조회
    public ProgressType getUserType(String email) {
        Member member = memberService.getMemberByEmail(email);
        return Optional.ofNullable(member.getProgressType())
            .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_TYPE_NON_EXISTENT));
    }

    //유저의 진행도 전체 조회
    @Transactional(readOnly = true)
    public ProgressByMainResponseDto getProgressDetails(String email) {
        Member member = memberService.getMemberByEmail(email);

        Progress progress = getProgressByMember(member);
        MainStep mainStep = getCurrentMainStep(progress);

        List<SubStepResponseDto> subStepList = subStepInfoService.getSubStepList(mainStep);
        return new ProgressByMainResponseDto(progress, mainStep, subStepList);
    }

    //특정 메인 단계만 조회
    @Transactional(readOnly = true)
    public ProgressByMainResponseDto getSubStepsByMainStep(String email,
        MainStepEnum mainStepEnum) {
        Member member = memberService.getMemberByEmail(email);
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);

        List<SubStepResponseDto> subStepList = subStepInfoService.getSubStepList(mainStep);
        return new ProgressByMainResponseDto(progress, mainStep, subStepList);
    }

    //특정 서브단계만 조회
    @Transactional(readOnly = true)
    public SubStepResponseDto getSubStepStatus(String email,
        MainStepEnum mainStepEnum,
        SubStepEnum subStepEnum) {
        Member member = memberService.getMemberByEmail(email);
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);
        SubStep subStep = getSubStepByEnum(mainStep, subStepEnum);

        String content = subStepInfoService.getSubStepContent(subStepEnum);
        return new SubStepResponseDto(subStep, content);
    }

    //진행도 업데이트
    @Transactional
    public SubStepResponseDto completeProgress(String email, MainStepEnum mainStepEnum,
        SubStepEnum subStepEnum) {

        Member member = memberService.getMemberByEmail(email);
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);
        SubStep subStep = getSubStepByEnum(mainStep, subStepEnum);

        MainStep currentMainStep = getCurrentMainStep(progress);

        if (!currentMainStep.getMainStepEnum().equals(mainStepEnum)) {
            throw new BusinessException(
                ErrorCode.PROGRESS_REQUEST_MAIN_STEP_IS_NOT_THE_CURRENT_STEP);
        }

        if (subStep.isCompletion()) {
            throw new BusinessException(ErrorCode.PROGRESS_ALREADY_COMPLETED);
        }

        subStep.markAsComplete();
        String content = subStepInfoService.getSubStepContent(subStepEnum);
        return new SubStepResponseDto(subStep, content);
    }

    //진행도 완료 취소
    @Transactional
    public SubStepResponseDto revertProgressToIncomplete(String email, MainStepEnum mainStepEnum,
        SubStepEnum subStepEnum) {
        Member member = memberService.getMemberByEmail(email);
        Progress progress = getProgressByMember(member);

        MainStep mainStep = getMainStepByEnum(progress, mainStepEnum);
        SubStep subStep = getSubStepByEnum(mainStep, subStepEnum);

        MainStep currentMainStep = getCurrentMainStep(progress);

        if (!currentMainStep.getMainStepEnum().equals(mainStepEnum)) {
            throw new BusinessException(
                ErrorCode.PROGRESS_REQUEST_MAIN_STEP_IS_NOT_THE_CURRENT_STEP);
        }

        if (!subStep.isCompletion()) {
            throw new BusinessException(ErrorCode.PROGRESS_ALREADY_INCOMPLETE);
        }
        subStep.markAsIncomplete();
        String content = subStepInfoService.getSubStepContent(subStepEnum);
        return new SubStepResponseDto(subStep, content);

    }

    public MainStep getCurrentMainStep(Progress progress) {
        MainStep mainStep = null;

        // 유저의 현재 메인 단계 설정
        for (MainStepEnum mainStepEnum : progress.getProgressType().getMainStepEnumList()) {
            MainStep step = getMainStepByEnum(progress, mainStepEnum);
            if (!step.isCompletion()) {
                mainStep = step;
                break;
            }
            mainStep = step; // 모든 단계가 완료되었을 때 마지막 단계로 설정
        }
        return mainStep;
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
