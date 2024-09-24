package boomerang.template.dto;

import boomerang.template.domain.TemplateColumn1;
import boomerang.template.domain.TemplateColumn2;
import boomerang.template.domain.TemplateDomain;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemplateResponseDto {
    private TemplateColumn1 templateColumn1;
    private TemplateColumn2 templateColumn2;

    // 생성자
    public TemplateResponseDto(TemplateColumn1 templateColumn1, TemplateColumn2 templateColumn2) {
        this.templateColumn1 = templateColumn1;
        this.templateColumn2 = templateColumn2;
    }

    /*
        Domain 값에서 Response 에 필요한 값들만 따로 추출하여 사용 가능

        Domain (id, col1, col2, col3) 이라면
        Response (col1, col2) 와 같이 불필요한 부분을 제거 또는
        Response (col1 + "원", col2 + "개") 와 같이 값 형태를 변경하는 데 사용된다
     */
    public static TemplateResponseDto of(TemplateDomain templateDomain) {
        return TemplateResponseDto.builder()
                .templateColumn1(templateDomain.getTemplateColumn1())
                .templateColumn2(templateDomain.getTemplateColumn2())
                .build();
    }
}
