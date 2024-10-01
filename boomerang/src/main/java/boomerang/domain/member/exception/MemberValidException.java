package boomerang.domain.member.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class MemberValidException extends DomainValidationException {
    public MemberValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
