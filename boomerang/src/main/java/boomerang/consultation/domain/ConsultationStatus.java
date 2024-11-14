package boomerang.consultation.domain;

public enum ConsultationStatus {
    RECEIVED("확정전"),
    PENDING("진행전"),
    ONGOING("진행중"),
    FINISHED("진행완료");

    private final String description;

    ConsultationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
