package boomerang.progress.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.member.domain.Member;
import boomerang.progress.domain.*;

import java.util.List;
import java.util.Set;

public class ProgressBStrategy implements ProgressStrategy {
    private static final ProgressType progressType = ProgressType.B;
    private static final Set<MainStepEnum> MAIN_STEPS =
            Set.of(MainStepEnum.MAIN_STEP_2);

    @Override
    public Progress makeProgress(Member member) {
        Progress progress = new Progress(member, progressType);
        List<MainStep> mainStepList = MAIN_STEPS.stream()
                .map(
                        mainStepEnum ->
                        {
                            MainStep mainStep = new MainStep(mainStepEnum,progress);
                            List<SubStep> subStepList = mainStepEnum.getSubStepEnumList()
                                    .stream()
                                    .map(subStepEnum -> new SubStep(mainStep, subStepEnum)).toList();

                            mainStep.registerSubStepList(subStepList);

                            return mainStep;
                        })
                .toList();

        progress.registerMainStepList(mainStepList);

        return progress;
    }

    @Override
    public void isValidMainStepForProgressType(MainStepEnum mainStepEnum) {
        if (!MAIN_STEPS.contains(mainStepEnum)) {
            throw new BusinessException(ErrorCode.PROGRESS_MAIN_INVALID);
        }
    }

}
