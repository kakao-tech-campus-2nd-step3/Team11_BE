package boomerang.domain.member.exception;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
    }
}
