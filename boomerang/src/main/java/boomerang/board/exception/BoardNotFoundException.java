package boomerang.board.exception;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;

public class BoardNotFoundException extends BusinessException {
    public BoardNotFoundException() {
        super(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
    }
}
