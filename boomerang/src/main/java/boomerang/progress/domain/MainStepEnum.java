package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public enum MainStepEnum {
    A1("전세권-설정-확인",List.of()),
    A2BC3("전세사기-피해자-신청",List.of()),
    A3("경매-신청하는-방법",List.of()),
    A4("배당-요구-신청",List.of()),
    A5("경매-결과",List.of()),
    BC1("계약-해지-내용-증명",List.of()),
    BC2("임차권-등기-명령",List.of()),
    B4("지급-명령-신청-전세-보증금-반환-소송-집행권원-취득",List.of()),
    B5("압류-이후-경매-진행",List.of()),
    C4("보증-이행-청구",List.of()),
    C5("주택-명도-퇴거",List.of());

    private final String mainStepName;
    private final List<SubStepEnum> subStepEnumList;

    MainStepEnum(String mainStepName, List<SubStepEnum> subStepEnumList) {
        this.mainStepName = mainStepName;
        this.subStepEnumList = subStepEnumList;
    }

    @JsonCreator
    public static MainStepEnum fromStepName(String stepName) {
        for (MainStepEnum mainStepEnum : MainStepEnum.values()) {
            if (mainStepEnum.mainStepName.equals(stepName)) {
                return mainStepEnum;
            }
        }
        throw new IllegalArgumentException(ErrorCode.PROGRESS_MAIN_INVALID_NAME.getMessage());
    }

    @JsonValue
    public String getMainStepName() {
        return mainStepName;
    }

    public List<SubStepEnum> getSubStepEnumList() {
        return subStepEnumList;
    }

    public boolean isMatchingMainAndSub(SubStepEnum subStepEnum) {
        return this.subStepEnumList.contains(subStepEnum);
    }

}
