package boomerang.consultation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleResponseListDto {

    private List<ScheduleMonthDto> ScheduleList = new ArrayList<>();

    public ScheduleResponseListDto(List<ScheduleMonthDto> scheduleList) {
        this.ScheduleList = scheduleList;
    }
}
