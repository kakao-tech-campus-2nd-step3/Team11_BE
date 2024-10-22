package boomerang.board.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AnonymousStatus {
    PRIVATE("PRIVATE"), PUBLIC("PUBLIC");

    private final String name;

    AnonymousStatus(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}

