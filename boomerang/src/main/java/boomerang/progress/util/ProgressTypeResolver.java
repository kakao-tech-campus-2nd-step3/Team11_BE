package boomerang.progress.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.dto.ProgressTypeRequestDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressTypeResolver {

    public static ProgressType checkType(ProgressTypeRequestDto progressTypeRequestDto) {
        Boolean isMemberInsureds = progressTypeRequestDto.getIsInsured();
        Boolean isMemberContractTerminated = progressTypeRequestDto.getIsContractTerminated();

        if (isMemberInsureds && isMemberContractTerminated) {
            return ProgressType.D;
        }
        if (!isMemberInsureds && isMemberContractTerminated) {
            return ProgressType.C;
        }
        if (isMemberInsureds && !isMemberContractTerminated) {
            return ProgressType.B;
        }
        if (!isMemberInsureds && !isMemberContractTerminated) {
            return ProgressType.A;
        }
        throw new BusinessException(ErrorCode.PROGRESS_TYPE_REQUEST_ERROR);
    }


}
