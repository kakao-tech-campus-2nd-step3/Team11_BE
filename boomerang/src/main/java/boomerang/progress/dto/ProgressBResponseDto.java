package boomerang.progress.dto;

import boomerang.progress.domain.ProgressB;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressBResponseDto extends ProgressResponseDto {

    private boolean step1;
    private boolean step2;
    private boolean step3;

    public ProgressBResponseDto(ProgressB progressB) {
        this.step1 = progressB.isStep1();
        this.step2 = progressB.isStep2();
        this.step3 = progressB.isStep3();
    }


}
