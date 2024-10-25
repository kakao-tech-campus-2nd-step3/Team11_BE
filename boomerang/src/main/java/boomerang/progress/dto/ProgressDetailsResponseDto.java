package boomerang.progress.dto;

import boomerang.progress.domain.MainStep;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import boomerang.progress.util.ProgressStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressDetailsResponseDto  {
    private ProgressType progressType; //유저의 타입
    private List<MainStepResponseDto> mainStepList;

    public ProgressDetailsResponseDto(Progress progress, List<MainStep> mainStepList) {
        this.progressType = progress.getProgressType();
        this.mainStepList = mainStepList
                .stream()
                .map(MainStepResponseDto::new)
                .toList();
    }
}
