package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MainStepEnum {
    MAIN_STEP_1( "mainStep1"),
    ;

    private final String mainStepName;

    MainStepEnum(String mainStepName) {
        this.mainStepName = mainStepName;
    }

    @JsonValue
    public String getSubStepName() {
        return mainStepName;
    }

    @JsonCreator
    public static MainStepEnum fromStepName(String stepName) {
        for (MainStepEnum mainStepEnum : MainStepEnum.values()) {
            if (mainStepEnum.mainStepName.equals(stepName)) {
                return mainStepEnum;
            }
        }
        throw new BusinessException(ErrorCode.PROGRESS_MAIN_INVALID_NAME);
    }

}
