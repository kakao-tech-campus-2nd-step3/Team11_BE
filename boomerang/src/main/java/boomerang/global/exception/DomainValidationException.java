package boomerang.global.exception;


import boomerang.global.response.ErrorCode;

import java.util.Arrays;

// Embedded 한 값의 Exception
//
public class DomainValidationException extends RuntimeException {
    private final ErrorCode errorCode;

    public DomainValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return errorCode.toString() + " : " + Arrays.toString(getStackTrace());
    }
}
