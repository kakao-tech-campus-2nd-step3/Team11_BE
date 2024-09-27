package boomerang.domain.member.domain;

import boomerang.domain.member.exception.MemberValidException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Password {
    @Column(name = "password")
    private String value;

    public Password() {

    }

    public Password(String value) {
        // 조건을 통해 valid한 값인지를 체크한다
        if (value.length() < 8) { // password가
            throw new MemberValidException(ErrorCode.DUPLICATE_EMAIL_ERROR);
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
        Password that = (Password) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
