package boomerang.like.dto;

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
public class LikeSummaryResponseDto {
    private int count;
    private boolean isLiked;

    public LikeSummaryResponseDto(int count, boolean isLiked) {
        this.count = count;
        this.isLiked = isLiked;
    }
}
