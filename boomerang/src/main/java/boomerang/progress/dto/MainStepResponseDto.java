package boomerang.progress.dto;

import boomerang.progress.domain.MainStep;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MainStepResponseDto {
    private String mainStepName;
    private Boolean completion;
    private List<SubStepResponseDto> subStepList;

    public MainStepResponseDto(MainStep mainStep) {
        this.mainStepName = mainStep.getMainName();
        this.completion = mainStep.getCompletion();
        this.subStepList = mainStep.getSubStepAllForResponse();
    }
}
