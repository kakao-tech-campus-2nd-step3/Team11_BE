package boomerang.progress.dto;


import boomerang.progress.domain.ProgressType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProgressTypeResponseDto {

    private ProgressType progressType;

    public ProgressTypeResponseDto(ProgressType progressType) {
        this.progressType = progressType;
    }
}
