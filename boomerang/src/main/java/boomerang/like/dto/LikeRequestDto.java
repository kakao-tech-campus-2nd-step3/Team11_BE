package boomerang.like.dto;

import boomerang.like.domain.LikeDomain;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class LikeRequestDto {

    private Member member;
    private Board board;

    // TemplateCreateServiceDto로 변환하는 메서드
    public LikeDomain toLikeDomain(Member member) {
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
