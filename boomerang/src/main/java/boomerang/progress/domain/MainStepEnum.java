package boomerang.progress.domain;

import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public enum MainStepEnum {
    AB1("계약-해지-내용-증명", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    AB2("임차권-등기-명령-신청", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    AB3("전세사기-피해자-센터-신청", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    B4("보증-이행-청구-서류-준비", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    B5("명도일-지정", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    A4("지급-명령-신청", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    A5("전세-보증금-반환-소송", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),

    // 전세권 계약한 사람의 단계
    C1("전세권-계약-여부-확인", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    C2("경매-유예-혹은-속행", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    C3("경매-공매-신청", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    C4("배당-요구-신청", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2)),
    C5("경매-낙찰-및-유찰-여부-확인", List.of(SubStepEnum.SUB_1, SubStepEnum.SUB_2));

    private final String mainStepName;
    private final List<SubStepEnum> subStepEnumList;

    MainStepEnum(String mainStepName, List<SubStepEnum> subStepEnumList) {
        this.mainStepName = mainStepName;
        this.subStepEnumList = subStepEnumList;
    }

//    @JsonCreator
//    public static MainStepEnum fromStepName(String stepName) {
//        for (MainStepEnum mainStepEnum : MainStepEnum.values()) {
//            if (mainStepEnum.mainStepName.equals(stepName)) {
//                return mainStepEnum;
//            }
//        }
//        throw new IllegalArgumentException(ErrorCode.PROGRESS_MAIN_INVALID_NAME.getMessage());
//    }

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
