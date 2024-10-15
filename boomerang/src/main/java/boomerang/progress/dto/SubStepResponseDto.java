package boomerang.progress.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;


@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubStepResponseDto {
    private String name;                  //보험가입여부
    private boolean completion;

    public SubStepResponseDto(String name, boolean completion) {
        this.name = name;
        this.completion = completion;
    }

}
