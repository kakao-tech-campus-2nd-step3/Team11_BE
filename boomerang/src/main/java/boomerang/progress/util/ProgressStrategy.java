package boomerang.progress.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.*;
import boomerang.progress.dto.SubStepResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStrategy {
    public static Progress updateProgress(Progress progress, MainStepEnum mainStepEnum, SubStepEnum subStepEnum) {
        return null;
    }

    public static List<MainStep> generateActiveMainStepsByType(Progress progress) {
        ProgressType progressType = progress.getProgressType();

        if (progressType.equals(ProgressType.A)) {
            return completeMainStepListByTypeA(progress);
        }
        if (progressType.equals(ProgressType.B)) {
            return completeMainStepListByTypeB(progress);
        }
        if (progressType.equals(ProgressType.C)) {
            return completeMainStepListByTypeC(progress);
        }
        return completeMainStepListByTypeD(progress);

    }

    private static List<MainStep> completeMainStepListByTypeA(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

    private static List<MainStep> completeMainStepListByTypeB(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

    private static List<MainStep> completeMainStepListByTypeC(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }

    private static List<MainStep> completeMainStepListByTypeD(Progress progress) {
        List<MainStep> mainSteps = new ArrayList<>();
        mainSteps.add(progress.getMainStepEx());
        return mainSteps;
    }


    public static MainStep findMainStepByEnum(Progress progress, MainStepEnum mainStepEnum) {
        if (mainStepEnum.equals(MainStepEnum.MAIN_STEP_1)) {
            return progress.getMainStepEx();
        }
        throw new BusinessException(ErrorCode.PROGRESS_MAIN_ERROR);
    }

    public static SubStepResponseDto findSubStepByEnum(Progress progress, SubStepEnum subStepEnum) {
        if (subStepEnum.equals(SubStepEnum.SUB_STEP_1)) {
            return new SubStepResponseDto(subStepEnum.getSubStepName(), progress.getMainStepEx().getSubStep1());
        }

        throw new BusinessException(ErrorCode.PROGRESS_SUB_ERROR);
    }


}
