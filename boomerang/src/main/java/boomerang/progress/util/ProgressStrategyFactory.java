package boomerang.progress.util;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
import boomerang.progress.domain.ProgressType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgressStrategyFactory {

    public static ProgressStrategy getStrategy(ProgressType type) {
        if (type.equals(ProgressType.A)) {
            return new ProgressAStrategy();
        }
        if (type.equals(ProgressType.B)) {
            return new ProgressBStrategy();
        }
        if (type.equals(ProgressType.C)) {
            return new ProgressCStrategy();
        }
        if (type.equals(ProgressType.D)) {
            return new ProgressDStrategy();
        }
        throw new BusinessException(ErrorCode.PROGRESS_REQUEST_ERROR);
    }
}
