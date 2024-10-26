package boomerang.progress.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.*;
import boomerang.progress.dto.SubStepDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStrategy {

    public static List<MainStep> getActiveMainStepList(Progress progress) {
        ProgressType progressType = progress.getProgressType();

        if (progressType.equals(ProgressType.A)) {
            return completeActiveMainStepListByTypeA(progress);
        }
        if (progressType.equals(ProgressType.B)) {
            return completeActiveMainStepListByTypeB(progress);
        }
        if (progressType.equals(ProgressType.C)) {
            return completeActiveMainStepListByTypeC(progress);
        }
        return completeActiveMainStepListByTypeD(progress);
    }


    public static MainStep getMainStep(Progress progress, MainStepEnum mainStepEnum) {
        if (mainStepEnum.equals(MainStepEnum.MAIN_STEP_1)) {
            return progress.getMainStepEx();
        }
        throw new BusinessException(ErrorCode.PROGRESS_MAIN_ERROR);
    }

    public static SubStepDto getSubStep(Progress progress, SubStepEnum subStepEnum) {
        if (subStepEnum.equals(SubStepEnum.SUB_STEP_1)) {
            return new SubStepDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep1());
        }

        if (subStepEnum.equals(SubStepEnum.SUB_STEP_2)) {
            return new SubStepDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep2());
        }

        throw new BusinessException(ErrorCode.PROGRESS_SUB_ERROR);
    }


    public static SubStepDto revertProgressToIncomplete(Progress progress, SubStepEnum subStepEnum) {
        //서브 단계가 늘어날 수록 if문이 늘어날 예정
        if (subStepEnum.equals(SubStepEnum.SUB_STEP_1)) {
            progress.getMainStepEx().updateSubStep1(false);
            return new SubStepDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep1());
        }
        if (subStepEnum.equals(SubStepEnum.SUB_STEP_2)) {
            progress.getMainStepEx().updateSubStep2(false);
            return new SubStepDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep2());
        }

        throw new BusinessException(ErrorCode.PROGRESS_SUB_ERROR);
    }

    public static SubStepDto completeProgress(Progress progress, SubStepEnum subStepEnum) {
        //서브 단계가 늘어날 수록 if문이 늘어날 예정
        if (subStepEnum.equals(SubStepEnum.SUB_STEP_1)) {
            progress.getMainStepEx().updateSubStep1(true);
            return new SubStepDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep1());
        }
        if (subStepEnum.equals(SubStepEnum.SUB_STEP_2)) {
            progress.getMainStepEx().updateSubStep2(true);
            return new SubStepDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep2());
        }

        throw new BusinessException(ErrorCode.PROGRESS_SUB_ERROR);
    }



    private static List<MainStep> completeActiveMainStepListByTypeA(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

    private static List<MainStep> completeActiveMainStepListByTypeB(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

    private static List<MainStep> completeActiveMainStepListByTypeC(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

    private static List<MainStep> completeActiveMainStepListByTypeD(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

}
