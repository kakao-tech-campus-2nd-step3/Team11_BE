package boomerang.board.domain;

public enum AnonymousStatus {
    PRIVATE("PRIVATE"), PUBLIC("PUBLIC");

    private final String name;

    AnonymousStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
