package boomerang.like.dto;

import boomerang.like.domain.LikeDomain;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class LikeRequestDto {
    private Member member;
    private Board board;

    // 생성자
    public LikeRequestDto(Member member, Board board) {
        this.member = member;
        this.board = board;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public LikeDomain toLikeDomain() {
        return LikeDomain.builder()
                .member(member)
                .board(board)
                .build();
    }

    public LikeDomain toLikeDomain(Long id) {
        return LikeDomain.builder()
                .id(id)
                .member(member)
                .board(board)
                .build();
    }
}
