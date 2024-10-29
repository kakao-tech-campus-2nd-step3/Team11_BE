package boomerang.progress.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public enum ProgressType {
    A("A타입", "보험 미가입 / 계약 미해지"),
    B("B타입", "보험 가입 / 계약 미해지"),
    C("C타입", "보험 미가입 / 계약 해지"),
    D("D타입", "보험 가입 / 계약 해지");

    private final String typeName;
    private final String description;

    ProgressType(String typeName, String description) {
        this.typeName = typeName;
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    //DB에는 타입명만 올라갈 수 있도록 오버라이딩
    @Override
    public String toString() {
        return typeName;
    }

    //JSON 반환시 전체 정보가 갈 수 있도록 제공
    @JsonValue
    public ProgressTypeJson getJson() {
        return new ProgressTypeJson(typeName, description);
    }


    // 내부 클래스로 JSON에서 사용할 객체 정의
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ProgressTypeJson {
        private final String typeName;
        private final String description;

        public ProgressTypeJson(String typeName, String description) {
            this.typeName = typeName;
            this.description = description;
        }

        public String getTypeName() {
            return typeName;
        }

        public String getDescription() {
            return description;
        }
    }
}