package boomerang.template.dto;

import boomerang.template.domain.TemplateColumn1;
import boomerang.template.domain.TemplateColumn2;
import boomerang.template.domain.TemplateDomain;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateRequestDto {
    private TemplateColumn1 templateColumn1;
    private TemplateColumn2 templateColumn2;

    // 생성자
    public TemplateRequestDto(TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    // TemplateCreateServiceDto로 변환하는 메서드
    public TemplateDomain toTemplateDomain() {
        return TemplateDomain.builder()
                .templateColumn1(templateColumn1)
                .templateColumn2(templateColumn2)
                .build();
    }

    public TemplateDomain toTemplateDomain(Long id) {
        return TemplateDomain.builder()
                .id(id)
                .templateColumn1(templateColumn1)
                .templateColumn2(templateColumn2)
                .build();
    }
}
