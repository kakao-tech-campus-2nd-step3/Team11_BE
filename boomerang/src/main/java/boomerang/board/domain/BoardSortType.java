package boomerang.board.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BoardSortType {
    ID("id"), LIKE("likeCount"), COMMENT("commentCount");

    private final String name;

    BoardSortType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}

