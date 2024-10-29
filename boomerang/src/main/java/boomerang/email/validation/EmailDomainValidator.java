package boomerang.email.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class EmailDomainValidator implements ConstraintValidator<EmailDomain, String> {
    private static final List<String> ALLOWED_DOMAINS = Arrays.asList(
        "pusan.ac.kr",
        "naver.com"
        // 새로운 도메인은 여기에 추가
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // null 체크는 @NotBlank가 처리
        }

        return ALLOWED_DOMAINS.stream()
            .anyMatch(domain -> value.endsWith("@" + domain));
    }
}