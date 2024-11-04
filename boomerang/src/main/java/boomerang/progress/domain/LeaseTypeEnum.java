package boomerang.progress.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaseTypeEnum {
    JEONSE("전세권"),
    RENTAL("임대차");

    private final String description;

    LeaseTypeEnum(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static LeaseTypeEnum fromDescription(String description) {
        for (LeaseTypeEnum type : LeaseTypeEnum.values()) {
            if (type.description.equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("계약 enum 생성중 오류 발생" + description);
    }

}
