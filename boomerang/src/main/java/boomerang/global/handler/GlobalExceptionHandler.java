package boomerang.global.handler;

import boomerang.global.exception.BusinessException;
import boomerang.global.exception.KakaoException;
import boomerang.global.response.ErrorCode;
import boomerang.global.response.ErrorResponseDto;
import boomerang.global.utils.ResponseHelper;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @Order(1)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(
        MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        String errorMessage = String.join(", ", errors);
        return ResponseHelper.createErrorResponse(ErrorCode.BAD_REQUEST, errorMessage);
    }

    @Order(1)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(
        ConstraintViolationException e) {
        log.error("Constraint violation: {}", e.getMessage());
        List<String> errors = e.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

        String errorMessage = String.join(", ", errors);
        return ResponseHelper.createErrorResponse(ErrorCode.BAD_REQUEST, errorMessage);
    }

    @Order(1)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException e) {
        log.error("Bind error: {}", e.getMessage());
        List<String> errors = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        String errorMessage = String.join(", ", errors);
        return ResponseHelper.createErrorResponse(ErrorCode.BAD_REQUEST, errorMessage);
    }

    @Order(3)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleException(IllegalStateException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return ResponseHelper.createErrorResponse(errorCode, e.getMessage());
    }

    
    @Order(2)
    @ExceptionHandler(KakaoException.class)
    public ResponseEntity<ErrorResponseDto> handleException(KakaoException e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        return ResponseHelper.createErrorResponse(errorCode, e.getStatus(), e.getMessage());
    }

  @Order(4)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error(Arrays.toString(e.getStackTrace()));
        ErrorCode errorCode = ErrorCode.UNEXPECTED_ERROR;
        return ResponseHelper.createErrorResponse(errorCode);
    }
}
