package boomerang.board.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BoardType {
    ENTIRE("ENTIRE"), SECRETE("SECRETE"), LOCATION("LOCATION"), STEP("STEP");

    private final String name;

    BoardType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}

