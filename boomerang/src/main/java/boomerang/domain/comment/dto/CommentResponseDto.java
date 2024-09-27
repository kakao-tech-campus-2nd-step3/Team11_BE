package boomerang.domain.comment.dto;

import boomerang.domain.comment.domain.Comment;
import boomerang.domain.member.domain.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;                        //댓글아이디
    private String authorName;              //작성자이름
    private String text;                    //댓글내용

    //json 반환시 [2024-05-06 12:34:03]
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;   //마지막으로 작성된 시간
    private boolean isAuthor;               //현재 유저가 작성자인지 여부(FE에서 사용할 수 있도록 )
    private boolean isEdited;               //수정여부

    //도메인을 기준으로 응답 객체를 만드는 부분
    public static CommentResponseDto of(Comment comment, Member loginUser) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthorName())
                .text(comment.getCommentText().getValue())
                .isAuthor(loginUser.equals(comment.getAuthor()))            //작성 여부 계산
                .isEdited(!comment.getCreatedAt().equals(comment.getUpdatedAt())) // 수정 여부 계산
                .lastModifiedAt(comment.getUpdatedAt() != null ? comment.getUpdatedAt() : comment.getCreatedAt()) //마지막으로 수정된 시간 제공
                .build();
    }

}
