package boomerang.global.response;

public record SimpleResultResponseDto(String code, String message) {
    public SimpleResultResponseDto(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }
}
