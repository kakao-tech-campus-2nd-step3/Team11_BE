package boomerang.like.dto;

import boomerang.like.domain.LikeColumn1;
import boomerang.like.domain.LikeColumn2;
import boomerang.like.domain.LikeDomain;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class LikeRequestDto {
    private LikeColumn1 likeColumn1;
    private LikeColumn2 likeColumn2;

    // 생성자
    public LikeRequestDto(LikeColumn1 likeColumn1, LikeColumn2 likeColumn2) {
        this.likeColumn1 = likeColumn1;
        this.likeColumn2 = likeColumn2;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public LikeDomain toTemplateDomain() {
        return LikeDomain.builder()
                .likeColumn1(likeColumn1)
                .likeColumn2(likeColumn2)
                .build();
    }

    public LikeDomain toTemplateDomain(Long id) {
        return LikeDomain.builder()
                .id(id)
                .likeColumn1(likeColumn1)
                .likeColumn2(likeColumn2)
                .build();
    }
}
