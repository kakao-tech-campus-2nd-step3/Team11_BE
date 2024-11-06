package boomerang.consultation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleResponseDto {
    private int month;
    private List<Map<String, List<Integer>>> dayList = new ArrayList<>();

    public ScheduleResponseDto(int month, List<Map<String, List<Integer>>> dayList) {
        this.month = month;
        this.dayList = dayList;
    }
}
