package boomerang.domain.member.domain;

import boomerang.domain.member.exception.MemberValidException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Insurance_status {
    @Column(name = "insurance_status")
    private String value;

    public Insurance_status() {

    }

    public Insurance_status(String value) {
        // 조건을 통해 valid한 값인지를 체크한다
        if (false) {
            throw new MemberValidException(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
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
        Insurance_status that = (Insurance_status) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
