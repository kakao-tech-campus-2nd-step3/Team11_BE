package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.Getter;

public enum SubStepEnum {
    SUB_1("주택임차권등기명령 신청서","content", List.of("성명", "주민등록번호", "주소", "연락가능한전화번호", "임대인성명", "임대인주소", "임대차계약일자", "임차보증금액", "주민등록일자", "임차범위", "점유개시일자", "확정일자")),
    SUB_2("서브단계2","content", List.of("input1", "input2"));

    private final String subStepName;
    @Getter
    private final String content;
    @Getter
    private final List<String> inputs;

    SubStepEnum(String subStepName,String content, List<String> inputs) {
        this.subStepName = subStepName;
        this.content = content;
        this.inputs = inputs;
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

    @JsonValue
    public String getSubStepName() {
        return subStepName;
    }
}
