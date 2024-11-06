package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.Getter;

public enum SubStepEnum {
    SUB_1("주택임차권등기명령 신청서","임대차가 끝난 후 보증금이 반환되지 않을 경우 관할법원에 신청할 수 있으며, 임차권 등기가 경료되면 기존의 대항력과 우선변제권이 유지되어, 임차주택에서 자유롭게 이사할 수 있게 하는 제도입니다.", List.of("성명", "주민등록번호", "주소", "연락가능한전화번호", "임대인성명", "임대인주소", "임대차계약일자", "임차보증금액", "주민등록일자", "임차범위", "점유개시일자", "확정일자", "제출법원명")),
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
