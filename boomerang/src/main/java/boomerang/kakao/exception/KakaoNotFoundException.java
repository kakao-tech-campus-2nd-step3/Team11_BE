package boomerang.kakao.exception;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;

public class KakaoNotFoundException extends BusinessException {
    public KakaoNotFoundException() {
        super(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
    }
}
