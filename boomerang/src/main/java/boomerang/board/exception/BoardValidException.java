package boomerang.board.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class BoardValidException extends DomainValidationException {
    public BoardValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
