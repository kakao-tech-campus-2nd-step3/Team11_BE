package boomerang.domain.comment.dto;

import boomerang.domain.comment.domain.CommentText;
import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequestDto {
    private CommentText commentText;

    //이 생성자를 통해서만 역직렬화 되도록 명시
    @JsonCreator //생성자를 통할 경우 @JsonNaming이 적용되지 않을 수 있어서 프로퍼티명을 명시
    public CommentRequestDto(@JsonProperty("comment_text") CommentText commentText) {
        if (commentText == null) {
            throw new BusinessException(ErrorCode.COMMENT_IS_NULL);
        }
        this.commentText = commentText;

    }
}

