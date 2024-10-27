package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubStepEnum {
    SUB_STEP_1("서브단계1"),
    SUB_STEP_2("서브단계2");

    private final String subStepName;

    SubStepEnum(String subStepName) {
        this.subStepName = subStepName;
    }

    @JsonValue
    public String getSubStepName() {
        return subStepName;
    }

    @JsonCreator
    public static SubStepEnum fromStepName(String stepName) {
        for (SubStepEnum subStepEnum : SubStepEnum.values()) {
            if (subStepEnum.subStepName.equals(stepName)) {
                return subStepEnum;
            }
        }
        throw new IllegalStateException(ErrorCode.PROGRESS_SUB_INVALID_NAME.getMessage());
    }
}
