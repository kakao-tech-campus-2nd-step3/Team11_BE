package boomerang.progress.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public enum ProgressType {
    A("A타입", "임대차 계약 / 보증 보험 X", List.of(MainStepEnum.AB1, MainStepEnum.AB2, MainStepEnum.AB3, MainStepEnum.A4, MainStepEnum.A5)),
    B("B타입", "임대차 계약 / 보증 보험 O", List.of(MainStepEnum.AB1, MainStepEnum.AB2, MainStepEnum.AB3, MainStepEnum.B4, MainStepEnum.B5)),
    C("C타입", "전세권 계약", List.of(MainStepEnum.C1, MainStepEnum.C2, MainStepEnum.C3, MainStepEnum.C4, MainStepEnum.C5));


    private final String typeName;
    private final String description;
    private final List<MainStepEnum> mainStepEnums;

    ProgressType(String typeName, String description, List<MainStepEnum> mainStepEnums) {
        this.typeName = typeName;
        this.description = description;
        this.mainStepEnums = mainStepEnums;
    }

    @JsonValue
    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    public List<MainStepEnum> getMainStepEnumList() {
        return mainStepEnums;
    }

    //DB에는 타입명만 올라갈 수 있도록 오버라이딩
    @Override
    public String toString() {
        return typeName;
    }



}