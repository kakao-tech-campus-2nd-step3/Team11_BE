package boomerang.prevention.enums;

import lombok.Getter;

@Getter
public enum ContractType {
    MONTHLY_RENT("월세"),
    CHARTER("전세");

    private final String description;

    ContractType(String description) {
        this.description = description;
    }
}