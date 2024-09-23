package boomerang.global.handler;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import io.jsonwebtoken.JwtException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @Order(0)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }

    @Order(1)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponseDto> handleJwtException(JwtException e) {
        System.out.println(e);
        ErrorCode errorCode = ErrorCode.JWT_ERROR;
        return ResponseHelper.createErrorResponse(errorCode);
    }

    @Order(2)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        System.out.println(e);
        ErrorCode errorCode = ErrorCode.UNEXPECTED_ERROR;
        return ResponseHelper.createErrorResponse(errorCode);
    }
}
