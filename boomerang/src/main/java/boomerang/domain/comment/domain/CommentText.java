package boomerang.domain.comment.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentText {

    @Column(name = "comment_text")
    private String value;

    @JsonCreator
    public CommentText(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.COMMENT_VALUE_IS_EMPTY);
        }
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
