package boomerang.like.dto;

import boomerang.like.domain.LikeColumn1;
import boomerang.like.domain.LikeColumn2;
import boomerang.like.domain.LikeDomain;
import lombok.Getter;

// 중간 Dto 변환이 필요한 경우 사용
// 일반적인 경우 Request 에서 바로 Domain으로 변환
@Getter
public class LikeServiceDto {
    private Long id;
    private LikeColumn1 likeColumn1;
    private LikeColumn2 likeColumn2;

    // 생성자
    public LikeServiceDto(Long id, LikeColumn1 likeColumn1, LikeColumn2 likeColumn2) {
        this.id = id;
        this.likeColumn1 = likeColumn1;
        this.likeColumn2 = likeColumn2;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public LikeDomain toTemplateDomain() {
        return new LikeDomain(id, likeColumn1, likeColumn2);
    }
}
