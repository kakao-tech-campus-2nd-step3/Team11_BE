package boomerang.progress.dto;

import boomerang.progress.domain.LeaseTypeEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressTypeRequestDto {

    private Boolean isInsured;                  //보험 가입 여부
    private LeaseTypeEnum leaseType;        //계약 종류
}
