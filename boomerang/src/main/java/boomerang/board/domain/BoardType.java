package boomerang.board.domain;

import lombok.Getter;

@Getter
public enum BoardType {
    ENTIRE("ENTIRE"), SECRETE("SECRETE"), LOCATION("LOCATION"), STEP("STEP");

    private final String name;

    BoardType(String name) {
        this.name = name;
    }
}
