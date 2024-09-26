package boomerang.like.dto;

import boomerang.like.domain.LikeDomain;
import lombok.Getter;

// 중간 Dto 변환이 필요한 경우 사용
// 일반적인 경우 Request 에서 바로 Domain으로 변환
@Getter
public class LikeServiceDto {
    private Long id;
    private Member member;
    private Board board;

    // 생성자
    public LikeServiceDto(Long id, Member member, Board board) {
        this.id = id;
        this.member = member;
        this.board = board;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public LikeDomain toTemplateDomain() {
        return new LikeDomain(id, member, board);
    }
}
