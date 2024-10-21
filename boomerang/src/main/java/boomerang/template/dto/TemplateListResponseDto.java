package boomerang.template.dto;

import boomerang.template.domain.TemplateDomain;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

    /*
        상황에 따라서는 List 로 담고 싶은 ResponseDto 와 기존의 ResponseDto의 형태가 조금 달라지는 경우가 있다

        ResponseDto
        { name, email, nickname }

        ResponseListDto
        {
            page:
            {
                { name, email, nickname, userStatus},
                { name, email, nickname, userStatus},
                { name, email, nickname, userStatus},
                ...
            }
        }

        이런 특수한 경우, 내부 클래스 Item 을 활용하여 List를 만들 수 있다
        그 경우에는 TemplateListNeedItem 클래스를 사용하여라

     */

@Getter
@Builder
@EqualsAndHashCode
public class TemplateListResponseDto {
    // page와 같은 추가적인 정보를 담는 필드
    private String val;
    private List<TemplateResponseDto> templateResponseDtoList;

    // 생성자
    public TemplateListResponseDto(String val, List<TemplateResponseDto> templateResponseDtoList) {
        this.val = val;
        this.templateResponseDtoList = templateResponseDtoList;
    }

    /*
        Domain 리스트를 받아서 각 요소를 TemplateResponseDto로 변환하여 List에 담는다
    */
    public static TemplateListResponseDto of(List<TemplateDomain> templateDomainList) {
        List<TemplateResponseDto> templateResponseDtoList = templateDomainList.stream()
                .map(TemplateResponseDto::of)
                .toList();

        return TemplateListResponseDto.builder()
                .val("some_value")  // 필요에 따라 값 설정
                .templateResponseDtoList(templateResponseDtoList)
                .build();
    }
}
