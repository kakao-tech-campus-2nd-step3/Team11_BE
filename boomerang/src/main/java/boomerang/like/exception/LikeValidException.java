package boomerang.like.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class LikeValidException extends DomainValidationException {
    public LikeValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
