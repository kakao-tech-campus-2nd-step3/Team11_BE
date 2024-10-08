package boomerang.member.domain;

import boomerang.global.response.ErrorCode;
import boomerang.template.exception.TemplateValidException;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Email {
    @Column(name = "email")
    private String value;


    public Email() {
    }

    public Email(String value) {
        // 조건을 통해 valid한 값인지를 체크한다
        if (false) {
            throw new TemplateValidException(ErrorCode.DUPLICATE_EMAIL_ERROR);
        }
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
