package boomerang.template.exception;

import boomerang.global.exception.DomainValidationException;
import boomerang.global.response.ErrorCode;

public class TemplateValidException extends DomainValidationException {
    public TemplateValidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
