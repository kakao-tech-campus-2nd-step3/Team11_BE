package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubStepEnum {
    SUB_STEP_1("sub_step1", MainStepEnum.MAIN_STEP_1),
    SUB_STEP_2("sub_step2", MainStepEnum.MAIN_STEP_1);

    private final String subStepName;
    private final MainStepEnum mainStepEnum;

    SubStepEnum(String subStepName, MainStepEnum mainStepEnum) {
        this.subStepName = subStepName;
        this.mainStepEnum = mainStepEnum;
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
        throw new BusinessException(ErrorCode.PROGRESS_SUB_INVALID_NAME);
    }


    public boolean isMatchingMainStep(MainStepEnum mainStepEnum) {
        return this.mainStepEnum == mainStepEnum;
    }

}
