package boomerang.like.exception;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;

public class LikeNotFoundException extends BusinessException {
    public LikeNotFoundException() {
        super(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
    }
}
