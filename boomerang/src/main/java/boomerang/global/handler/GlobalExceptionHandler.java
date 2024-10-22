package boomerang.global.handler;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Order(0)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        log.error(e.toString());
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }

    @Order(1)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException e) {
        log.error(e.toString());
        ErrorCode errorCode = ErrorCode.JWT_ERROR;
        return ResponseHelper.createErrorResponse(errorCode);
    }

    @Order(2)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorCode errorCode = ErrorCode.UNEXPECTED_ERROR;
        return ResponseHelper.createErrorResponse(errorCode);
    }
}
