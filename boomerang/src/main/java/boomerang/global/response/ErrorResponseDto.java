package boomerang.global.response;

public record ErrorResponseDto(String code, String message) {
    public ErrorResponseDto(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
}
