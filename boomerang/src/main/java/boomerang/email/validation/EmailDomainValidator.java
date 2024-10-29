package boomerang.email.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailDomainValidator implements ConstraintValidator<EmailDomain, String> {
    private static final String ALLOWED_DOMAIN = "@pusan.ac.kr";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // null 체크는 @NotBlank가 처리
        }
        return value.endsWith(ALLOWED_DOMAIN);
    }
}