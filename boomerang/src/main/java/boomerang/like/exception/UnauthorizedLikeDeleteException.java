package boomerang.like.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class UnauthorizedLikeDeleteException extends DomainValidationException {
    public UnauthorizedLikeDeleteException() {
        super(ErrorCode.NOT_MEMBERS_LIKE_ERROR);
    }
}
