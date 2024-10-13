package boomerang.progress.dto;

import boomerang.progress.domain.ProgressA;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressAResponseDto extends ProgressResponseDto {

    private boolean step1;
    private boolean step2;
    private boolean step3;

    public ProgressAResponseDto(ProgressA progressA) {
        this.step1 = progressA.isStep1();
        this.step2 = progressA.isStep2();
        this.step3 = progressA.isStep3();
    }
}
