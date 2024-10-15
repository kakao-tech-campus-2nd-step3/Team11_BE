package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MainStep {
    MAIN_STEP_1( "mainStep1"),
    ;

    private final String mainStepName;

    MainStep(String mainStepName) {
        this.mainStepName = mainStepName;
    }

    @JsonValue
    public String getSubStepName() {
        return mainStepName;
    }

    @JsonCreator
    public static MainStep fromStepName(String stepName) {
        for (MainStep mainStep : MainStep.values()) {
            if (mainStep.mainStepName.equals(stepName)) {
                return mainStep;
            }
        }
        throw new BusinessException(ErrorCode.PROGRESS_MAIN_INVALID_NAME);
    }


}
