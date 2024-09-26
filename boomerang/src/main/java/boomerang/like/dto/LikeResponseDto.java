package boomerang.like.dto;

import boomerang.like.domain.LikeDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponseDto {
    private Member member;
    private Board board;

    // 생성자
    public LikeResponseDto(Member member, Board board) {
        this.member = member;
        this.board = board;
    }

    /*
        Domain 값에서 Response 에 필요한 값들만 따로 추출하여 사용 가능

        Domain (id, col1, col2, col3) 이라면
        Response (col1, col2) 와 같이 불필요한 부분을 제거 또는
        Response (col1 + "원", col2 + "개") 와 같이 값 형태를 변경하는 데 사용된다
     */
    public static LikeResponseDto of(LikeDomain likeDomain) {
        return LikeResponseDto.builder()
                .member(likeDomain.getMember())
                .board(likeDomain.getBoard())
                .build();
    }
}
