package boomerang.comment.dto;

import boomerang.comment.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponseDto {
    private Long id;                        //댓글아이디
    private String writerEmail;              //작성자이름
    private String writerName;
    private String text;                    //댓글내용

    //json 반환시 [2024-05-06 12:34:03]
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;   //마지막으로 작성된 시간
    private boolean isEdited;               //수정여부

    //도메인을 기준으로 응답 객체를 만드는 부분
    public CommentResponseDto (Comment comment) {
        this.id = comment.getId();
        this.writerEmail = comment.getAuthorEmail();
        this.writerName = comment.getAuthorName();
        this.text = comment.getText();
        this.isEdited = !comment.getCreatedAt().equals(comment.getUpdatedAt()); // 수정 여부 계산
        this.lastModifiedAt = comment.getUpdatedAt() != null ? comment.getUpdatedAt() : comment.getCreatedAt(); //마지막으로 수정된 시간 제공
    }

}
