package boomerang.progress.dto;

import boomerang.progress.domain.SubStep;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;


@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubStepResponseDto {
    private String name;                  //보험가입여부
    private boolean completion;
    private String content;

    public SubStepResponseDto(SubStep subStep) {
        this.name = subStep.getName();
        this.completion = subStep.isCompletion();
        this.content = subStep.getSubStepEnum().getContent();
    }

}
