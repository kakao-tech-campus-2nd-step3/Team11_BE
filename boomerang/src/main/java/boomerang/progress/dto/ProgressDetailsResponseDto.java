package boomerang.progress.dto;

import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressDetailsResponseDto  {
    private ProgressType progressType; //유저의 타입
    private List<MainStepResponseDto> mainStepList;

    public ProgressDetailsResponseDto(Progress progress) {
        this.progressType = progress.getProgressType();
        this.mainStepList = progress.getActivatedMainList()
                .stream()
                .map(MainStepResponseDto::new)
                .toList();
    }
}
