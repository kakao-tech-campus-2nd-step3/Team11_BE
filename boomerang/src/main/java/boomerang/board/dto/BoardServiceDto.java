package boomerang.board.dto;

import boomerang.board.domain.TemplateColumn1;
import boomerang.board.domain.TemplateColumn2;
import boomerang.board.domain.BoardDomain;
import lombok.Getter;

// 중간 Dto 변환이 필요한 경우 사용
// 일반적인 경우 Request 에서 바로 Domain으로 변환
@Getter
public class BoardServiceDto {
    private Long id;
    private TemplateColumn1 templateColumn1;
    private TemplateColumn2 templateColumn2;

    // 생성자
    public BoardServiceDto(Long id, TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.id = id;
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public BoardDomain toTemplateDomain() {
        return new BoardDomain(id, templateColumn1, templateColumn2);
    }
}
