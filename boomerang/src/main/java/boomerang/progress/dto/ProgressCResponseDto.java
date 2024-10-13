package boomerang.progress.dto;

import boomerang.progress.domain.ProgressC;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressCResponseDto extends ProgressResponseDto {

    private boolean step1;
    private boolean step2;
    private boolean step3;

    public ProgressCResponseDto(ProgressC progressC) {
        this.step1 = progressC.isStep1();
        this.step2 = progressC.isStep2();
        this.step3 = progressC.isStep3();
    }
}
