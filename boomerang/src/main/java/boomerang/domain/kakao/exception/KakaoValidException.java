package boomerang.domain.kakao.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class KakaoValidException extends DomainValidationException {
    public KakaoValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
