package boomerang.progress.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.domain.LeaseTypeEnum;
import boomerang.progress.dto.ProgressTypeRequestDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressTypeResolver {

    public static ProgressType checkType(ProgressTypeRequestDto progressTypeRequestDto) {
        Boolean isMemberInsureds = progressTypeRequestDto.getIsInsured();
        LeaseTypeEnum leaseType = progressTypeRequestDto.getLeaseType();


        if (!isMemberInsureds && leaseType.equals(LeaseTypeEnum.RENTAL)) { //보험가입 X, 임대차계약
            return ProgressType.B;
        }
        if (isMemberInsureds && leaseType.equals(LeaseTypeEnum.RENTAL)) { //보험가입 O, 임대차계약
            return ProgressType.C;
        }
        if (leaseType.equals(LeaseTypeEnum.JEONSE)) {
            return ProgressType.A;
        }
        throw new BusinessException(ErrorCode.PROGRESS_TYPE_REQUEST_ERROR);
    }


}
