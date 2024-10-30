package boomerang.progress.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SubStepEnum {
    SUB_1("서브단계1","content"),
    SUB_2("서브단계2","content");

    private final String subStepName;
    private final String content;

    SubStepEnum(String subStepName,String content) {
        this.subStepName = subStepName;
        this.content = content;
    }

//    @JsonCreator
//    public static SubStepEnum fromStepName(String stepName) {
//        for (SubStepEnum subStepEnum : SubStepEnum.values()) {
//            if (subStepEnum.subStepName.equals(stepName)) {
//                return subStepEnum;
//            }
//        }
//        throw new IllegalStateException(ErrorCode.PROGRESS_SUB_INVALID_NAME.getMessage());
//    }

    @JsonValue
    public String getSubStepName() {
        return subStepName;
    }

    public String getContent() {
        return content;
    }
}
