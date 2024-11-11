package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;

public enum MainStepEnum {
    A1("전세권-설정-확인", List.of(SubStepEnum.A1_1)),
    A2BC3("전세사기-피해자-신청",
        List.of(SubStepEnum.A2BC3_1, SubStepEnum.A2BC3_2, SubStepEnum.A2BC3_3, SubStepEnum.A2BC3_4, SubStepEnum.A2BC3_5, SubStepEnum.A2BC3_6, SubStepEnum.A2BC3_7,
            SubStepEnum.A2BC3_8, SubStepEnum.A2BC3_9, SubStepEnum.A2BC3_10, SubStepEnum.A2BC3_11)),
    A3("경매-신청하는-방법", List.of(SubStepEnum.A3_1, SubStepEnum.A3_2)),
    A4("배당-요구-신청", List.of(SubStepEnum.A4_1, SubStepEnum.A4_2, SubStepEnum.A4_3, SubStepEnum.A4_4,
        SubStepEnum.A4_5, SubStepEnum.A4_6)),
    A5("경매-결과", List.of(SubStepEnum.A5_1, SubStepEnum.A5_2, SubStepEnum.A5_3)),
    BC1("계약-해지-내용-증명",
        List.of(SubStepEnum.BC1_1, SubStepEnum.BC1_2, SubStepEnum.BC1_3, SubStepEnum.BC1_4,
            SubStepEnum.BC1_5)),
    BC2("임차권-등기-명령",
        List.of(SubStepEnum.BC2_1, SubStepEnum.BC2_2, SubStepEnum.BC2_3, SubStepEnum.BC2_4,
            SubStepEnum.BC2_5, SubStepEnum.BC2_6, SubStepEnum.BC2_7)),
    B4("지급-명령-신청-전세-보증금-반환-소송-집행권원-취득",
        List.of(SubStepEnum.B4_1, SubStepEnum.B4_2, SubStepEnum.B4_3, SubStepEnum.B4_4,
            SubStepEnum.B4_5)),
    B5("압류-이후-경매-진행",
        List.of(SubStepEnum.B5_1, SubStepEnum.B5_2, SubStepEnum.B5_3, SubStepEnum.B5_4)),
    C4("보증-이행-청구", List.of(SubStepEnum.C4_1, SubStepEnum.C4_2)),
    C5("주택-명도-퇴거", List.of(SubStepEnum.C5_1, SubStepEnum.C5_2, SubStepEnum.C5_3));

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
