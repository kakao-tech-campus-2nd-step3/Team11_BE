package boomerang.progress.util;


import boomerang.member.domain.Member;
import boomerang.progress.domain.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressUtil {
    public static Progress makeProgress(ProgressType progressType, Member member) {
        List<MainStepEnum> mainStepEnumList = progressType.getMainStepEnumList();

        Progress progress = new Progress(member, progressType);
        List<MainStep> mainStepList = mainStepEnumList.stream()
                .map(
                        mainStepEnum ->
                        {
                            MainStep mainStep = new MainStep(mainStepEnum, progress);
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
