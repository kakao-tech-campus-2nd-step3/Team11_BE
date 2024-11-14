package boomerang.email.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmailVerificationRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        // given
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto("test@naver.com", "123456");

        // when
        Set<ConstraintViolation<EmailVerificationRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void whenEmailDomainInvalid_thenViolation() {
        // given
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto("test@invalid.com", "123456");

        // when
        Set<ConstraintViolation<EmailVerificationRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations)
                .extracting("message")
                .containsOnly("허용되지 않는 이메일 도메인입니다");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void whenVerificationCodeEmpty_thenViolation(String code) {
        // given
        EmailVerificationRequestDto dto = new EmailVerificationRequestDto("test@naver.com", code);

        // when
        Set<ConstraintViolation<EmailVerificationRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations)
                .extracting("message")
                .containsOnly("인증 코드는 필수입니다");
    }
}
