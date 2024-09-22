package boomerang.template.dto;

import boomerang.template.domain.TemplateColumn1;
import boomerang.template.domain.TemplateColumn2;
import lombok.Getter;

@Getter
public class TemplateCreateRequestDto {
    private TemplateColumn1 templateColumn1;
    private TemplateColumn2 templateColumn2;

    // 생성자
    public TemplateCreateRequestDto(TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public TemplateServiceDto toTemplateCreateServiceDto() {
        return new TemplateServiceDto(null, templateColumn1, templateColumn2);
    }

    public TemplateServiceDto toTemplateCreateServiceDto(Long id) {
        return new TemplateServiceDto(id, templateColumn1, templateColumn2);
    }
}
