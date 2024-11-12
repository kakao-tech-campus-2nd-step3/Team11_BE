package boomerang.documents.dto;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.SubStepEnum;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentRequestDto {

    private String subStep; //한글로
    private Map<String, String> formData;

    public void validate() {
        if (formData == null) {
            throw new BusinessException(ErrorCode.DOCUMENT_MISSING_FIELDS);
        }
    }
}