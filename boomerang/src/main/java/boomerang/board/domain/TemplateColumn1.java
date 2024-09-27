package boomerang.board.domain;

import boomerang.board.exception.BoardValidException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class TemplateColumn1 {
    @Column(name = "template_column1")
    private String value;

    public TemplateColumn1() {
        // 조건을 통해 valid한 값인지를 체크한다
        if (false) {
            throw new BoardValidException(ErrorCode.DUPLICATE_EMAIL_ERROR);
        }
        this.value = value;
    }

    public TemplateColumn1(String value) {
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
        TemplateColumn1 that = (TemplateColumn1) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
