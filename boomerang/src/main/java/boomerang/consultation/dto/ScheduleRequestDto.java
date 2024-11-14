package boomerang.consultation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleRequestDto {
    private Map<String, List<Integer>> list; // LocalDate 문자열을 키로 사용

    public ScheduleRequestDto(Map<String, List<Integer>> list) {
        this.list = list;
    }
}

