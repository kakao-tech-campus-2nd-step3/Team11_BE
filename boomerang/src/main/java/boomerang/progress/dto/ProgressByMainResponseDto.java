package boomerang.progress.dto;

import boomerang.progress.domain.MainStep;
import boomerang.progress.domain.MainStepEnum;
import boomerang.progress.domain.Progress;
import boomerang.progress.domain.ProgressType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressByMainResponseDto {

    private ProgressType progressType;                      //유저의 타입
    private MainStepEnum currentMainStep;      //현재 메인 단계
    private List<MainStepResponseDto> mainStepList;         //메인단계 리스트
    private List<SubStepResponseDto> subStepList;           //서브리스트

    public ProgressByMainResponseDto(Progress progress, MainStep mainStep,
        List<SubStepResponseDto> subStepList) {
        this.progressType = progress.getProgressType();
        this.currentMainStep = mainStep.getMainStepEnum();
        this.mainStepList = progress.getMainStepList()
            .stream()
            .map(MainStepResponseDto::new)
            .toList();
        this.subStepList = subStepList;
    }
}
