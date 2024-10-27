package boomerang.like.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class DuplicateLikeException extends DomainValidationException {
    public DuplicateLikeException() {
        super(ErrorCode.DUPLICATE_LIKE_ERROR);
    }
}
