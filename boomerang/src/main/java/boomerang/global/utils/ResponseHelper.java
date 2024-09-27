package boomerang.global.utils;

import boomerang.global.response.ErrorCode;
import boomerang.global.response.ErrorResponseDto;
import org.springframework.http.ResponseEntity;


public class ResponseHelper {

    // 생성자 방지
    private ResponseHelper() {
    }

    public static ResponseEntity<ErrorResponseDto> createErrorResponse(ErrorCode errorCode) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errorCode);
        return ResponseEntity.status(errorCode.getStatus())
                .body(errorResponseDto);
    }
}