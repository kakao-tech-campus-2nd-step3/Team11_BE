package boomerang.consultation.dto;

import boomerang.consultation.domain.Schedule;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleResponseDto {
    private List<Schedule> scheduleList;

    public ScheduleResponseDto(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
