package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.Getter;

public enum SubStepEnum {
    SUB_1("주택 임차권 등기명령 신청서","임대차가 끝난 후 보증금이 반환되지 않을 경우 관할법원에 신청할 수 있으며, 임차권 등기가 경료되면 기존의 대항력과 우선변제권이 유지되어, 임차주택에서 자유롭게 이사할 수 있게 하는 제도입니다.", List.of("성명", "주민등록번호", "주소", "연락가능한 전화번호", "임대인 성명", "임대인 주소", "임대차 계약일자", "임차보증금액", "주민등록일자", "임차범위", "점유개시일자", "확정일자", "제출법원명")),
    SUB_2("전세사기 피해자 결정 신청서(국토교통부)","전세사기 피해자로 인정받기 위한 필요 제출 서류 입니다.", List.of("성명", "생년월일", "주민등록지", "별도 우편 수령지 주소", "전화번호", "전자 우편주소", "대리인 성명", "대리인 생년월일", "대리인 주소", "대리인 전화번호", "대리인 전자우편주소", "전세사기 피해 주택 지번주소", "전임대인 성명", "전임대인 생년월일", "현임대인 성명", "현임대인 생년월일", "계약일자", "전월세구분(전세/보증부 월세)", "계약기간", "선순위 담보권(여/부)", "선순위 담보권자(금융기관/개인)", "압류(여/부)", "압류권자(국가 또는 지방자치단체/이외의 자)", "주택 유형(아파트/오피스텔/다세대/연립/단독/다중/다가구/기타)", "대항력 발생일", "전입일자", "점유일자", "확정일자", "임차권 등기명령 사건번호", "(거주/퇴거)", "임차보증금", "월세", "파산·회생 사건번호", "경매 사건번호", "공매 물건관리번호", "압류 사건번호", "집행 권원 확보 여부(여/부)", "임대인등의 기망 이유 혹은 수사개시 등", "경매 배당요구 여부(여/부)", "공매 배분요구 여부(여/부)", "경매·매각 유예·정지 긴급 여부(여/부)"));

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
