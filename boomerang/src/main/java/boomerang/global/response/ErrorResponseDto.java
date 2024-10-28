package boomerang.global.response;

public record ErrorResponseDto(String code, String message) {
    public ErrorResponseDto(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public ErrorResponseDto(ErrorCode errorCode, String message) {
        this(errorCode.getCode(), message);
    }
}
