package boomerang.like.dto;

import boomerang.like.domain.Like;
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
public class LikeResponseDto {
    private Long id;                        // 좋아요 ID
    private String memberName;              // 좋아요를 누른 사용자 이름
    private Long boardId;                   // 게시글 ID

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;        // 좋아요가 생성된 시간
    private boolean isOwner;                // 현재 사용자가 좋아요의 소유자인지 여부

    public LikeResponseDto(Like like, boolean isOwner) {
        this.id = like.getId();
        this.memberName = like.getMember().getNickname();
        this.boardId = like.getBoard().getId();
        this.createdAt = like.getCreatedAt();
        this.isOwner = isOwner;
    }
}