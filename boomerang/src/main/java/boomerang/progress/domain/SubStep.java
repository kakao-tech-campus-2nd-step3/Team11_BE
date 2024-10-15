package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubStep {
    SUB_STEP_1("sub_step1", MainStep.MAIN_STEP_1),
    SUB_STEP_2("sub_step2", MainStep.MAIN_STEP_1);

    private final String subStepName;
    private final MainStep mainStep;

    SubStep(String subStepName, MainStep mainStep) {
        this.subStepName = subStepName;
        this.mainStep = mainStep;
    }

    @JsonValue
    public String getSubStepName() {
        return subStepName;
    }

    @JsonCreator
    public static SubStep fromStepName(String stepName) {
        for (SubStep subStep : SubStep.values()) {
            if (subStep.subStepName.equals(stepName)) {
                return subStep;
            }
        }
        throw new BusinessException(ErrorCode.PROGRESS_SUB_INVALID_NAME);
    }


    public boolean isMatchingMainStep(MainStep mainStep) {
        return this.mainStep == mainStep;
    }

}
