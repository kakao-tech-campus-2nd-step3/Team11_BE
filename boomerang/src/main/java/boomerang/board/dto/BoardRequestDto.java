package boomerang.board.dto;

import boomerang.board.domain.TemplateColumn1;
import boomerang.board.domain.TemplateColumn2;
import boomerang.board.domain.BoardDomain;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class BoardRequestDto {
    private TemplateColumn1 templateColumn1;
    private TemplateColumn2 templateColumn2;

    // 생성자
    public BoardRequestDto(TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public BoardDomain toTemplateDomain() {
        return BoardDomain.builder()
                .templateColumn1(templateColumn1)
                .templateColumn2(templateColumn2)
                .build();
    }

    public BoardDomain toTemplateDomain(Long id) {
        return BoardDomain.builder()
                .id(id)
                .templateColumn1(templateColumn1)
                .templateColumn2(templateColumn2)
                .build();
    }
}
