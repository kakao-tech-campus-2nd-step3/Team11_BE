package boomerang.progress.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public enum MainStepEnum {
    MAIN_STEP_1("진행도 1단계", List.of(SubStepEnum.SUB_STEP_1, SubStepEnum.SUB_STEP_2)),
    ;

    private final String mainStepName;
    private final List<SubStepEnum> subStepEnumList;

    MainStepEnum(String mainStepName, List<SubStepEnum> subStepEnumList) {
        this.mainStepName = mainStepName;
        this.subStepEnumList = subStepEnumList;
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

    public boolean isMatchingMainAndSub(SubStepEnum subStepEnum) {
        return this.subStepEnumList.contains(subStepEnum);
    }

}
