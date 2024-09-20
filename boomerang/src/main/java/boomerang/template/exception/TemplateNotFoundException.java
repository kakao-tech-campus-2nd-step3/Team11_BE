package boomerang.template.exception;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;

public class TemplateNotFoundException extends BusinessException {
    public TemplateNotFoundException() {
        super(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
    }
}
