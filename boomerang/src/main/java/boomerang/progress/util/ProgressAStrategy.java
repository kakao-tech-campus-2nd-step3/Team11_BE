package boomerang.progress.util;


import boomerang.member.domain.Member;
import boomerang.progress.domain.*;

import java.util.List;

public class ProgressAStrategy implements ProgressStrategy {
    @Override
    public Progress makeProgress(Member member) {
        ProgressType progressType = ProgressType.A;
        List<MainStepEnum> mainStepEnumList = progressType.getMainStepEnums();

        Progress progress = new Progress(member, progressType);
        List<MainStep> mainStepList = mainStepEnumList.stream()
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
}
