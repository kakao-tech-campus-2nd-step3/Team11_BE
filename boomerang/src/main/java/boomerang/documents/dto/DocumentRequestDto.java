package boomerang.documents.dto;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.SubStepEnum;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDto {

    private String subStep;
    private Map<String, String> formData;

    public void validate() {
        SubStepEnum subStepEnum = SubStepEnum.fromStepName(subStep); // String을 Enum으로 변환

        if (subStepEnum == null) {
            throw new BusinessException(ErrorCode.DOCUMENT_TYPE_REQUIRED);
        }

        List<String> requiredFields = subStepEnum.getInputs();
        Set<String> providedFields = formData.keySet();

        // 필수 필드 검증
        Set<String> missingFields = new HashSet<>(requiredFields);
        missingFields.removeAll(providedFields);

        if (!missingFields.isEmpty()) {
            throw new BusinessException(ErrorCode.DOCUMENT_MISSING_FIELDS);
        }
    }

    public SubStepEnum getSubStepEnum() {
        return SubStepEnum.fromStepName(subStep);
    }
}