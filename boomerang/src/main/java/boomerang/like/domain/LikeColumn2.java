package boomerang.like.domain;

import boomerang.global.response.ErrorCode;
import boomerang.like.exception.LikeValidException;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LikeColumn2 {
    @Column(name = "template_column2")
    private String value;

    public LikeColumn2() {
        // 조건을 통해 valid한 값인지를 체크한다
        if (false) {
            throw new LikeValidException(ErrorCode.TEMPLATE_NOT_FOUND_ERROR);
        }
        this.value = value;
    }

    public LikeColumn2(String value) {
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
        LikeColumn2 that = (LikeColumn2) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
