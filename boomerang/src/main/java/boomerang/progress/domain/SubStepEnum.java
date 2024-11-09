package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SubStepEnum {
    A1_1("전세권-설정-알아보기"),
    A2BC3_1("전세사기-피해자-결정-알아보기"),
    A2BC3_2("전세사기-피해자-신청-조건-해당-알아보기"),
    A2BC3_3("온라인-오프라인-신청하기"),
    A2BC3_4("결정-신청서"),
    A2BC3_5("임대차-계약서-사본"),
    A2BC3_6("주민등록-초본"),
    A2BC3_7("개인-정보-수집-및-이용-동의서"),
    A2BC3_8("임대인의-파산선고-결정문-또는-회생개시-결정문-사본"),
    A2BC3_9("경매-공매개시-관련-서류-사본-1부"),
    A2BC3_10("집행권원"),
    A2BC3_11("임차권등기-서류"),
    A3_1("경매에-대해서-알아보기"),
    A3_2("경매-신청하기"),
    A4_1("배당-요구-알아보기"),
    A4_2("배당금-신청하기"),
    A4_3("경매-정보-확인하기"),
    A4_4("전입-날짜가-기재된-주민등록표-등-초본"),
    A4_5("확정일자가-나온-임대차-계약서"),
    A4_6("다가구-주택의-경우-내-점유-부분이-표시된-건물-도면"),
    A5_1("경매-낙찰에-대해-알아보기"),
    A5_2("경매-낙찰-이후-알아보기"),
    A5_3("경매-유찰-이후-알아보기"),
    BC1_1("계약해지-내용증명-알아보기"),
    BC1_2("계약해지-내용증명-보내기"),
    BC1_3("최고통지-내용증명-알아보기"),
    BC1_4("최고통지-내용증명-보내기"),
    BC1_5("내용-증명-반송됐을때-해결-방법"),
    BC2_1("임차권-등기-명령-알아보기"),
    BC2_2("임차권-등기-명령-신청하기"),
    BC2_3("임대차-계약서"),
    BC2_4("주민등록등본-또는-초본"),
    BC2_5("건물등기부등본-제출용"),
    BC2_6("계약-종료를-증명-자료"),
    BC2_7("주택임차권-등기-명령-서류"),
    B4_1("지급-명령-신청-알아보기"),
    B4_2("지급-명령-신청방법"),
    B4_3("전세-보증금-반환-소송이란"),
    B4_4("전세-보증금-반환-소송-하는-방법"),
    B4_5("집행권원-취득"),
    B5_1("부동산-압류-알아보기"),
    B5_2("부동산-압류-진행하기"),
    B5_3("부동산-압류-이후-경매-진행-과정-알아보기"),
    B5_4("경매-이후-배당금-수령시-주의점-알아보기"),
    C4_1("보증-이행-청구-알아보기"),
    C4_2("보증-기관별-보증-이행-신청하기"),
    C5_1("주택-명도-알아보기"),
    C5_2("이사날짜-확인하기"),
    C5_3("명도-증빙-자료-제출하기");

    private final String subStepName;

    SubStepEnum(String subStepName) {
        this.subStepName = subStepName;
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
