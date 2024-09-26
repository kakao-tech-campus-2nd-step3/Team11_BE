package boomerang.like.dto;

import boomerang.like.domain.LikeColumn1;
import boomerang.like.domain.LikeColumn2;
import boomerang.like.domain.LikeDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDto {
    private LikeColumn1 likeColumn1;
    private LikeColumn2 likeColumn2;

    // 생성자
    public LikeResponseDto(LikeColumn1 likeColumn1, LikeColumn2 likeColumn2) {
        this.likeColumn1 = likeColumn1;
        this.likeColumn2 = likeColumn2;
    }

    /*
        Domain 값에서 Response 에 필요한 값들만 따로 추출하여 사용 가능

        Domain (id, col1, col2, col3) 이라면
        Response (col1, col2) 와 같이 불필요한 부분을 제거 또는
        Response (col1 + "원", col2 + "개") 와 같이 값 형태를 변경하는 데 사용된다
     */
    public static LikeResponseDto of(LikeDomain likeDomain) {
        return LikeResponseDto.builder()
                .likeColumn1(likeDomain.getLikeColumn1())
                .likeColumn2(likeDomain.getLikeColumn2())
                .build();
    }
}
