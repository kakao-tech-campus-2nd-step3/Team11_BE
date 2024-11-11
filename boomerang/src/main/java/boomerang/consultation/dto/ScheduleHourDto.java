package boomerang.consultation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 예약된 특정 시간을 나타내는 HourSchedule 클래스
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleHourDto {
    private int hour;

    public ScheduleHourDto(int hour) {
        this.hour = hour;
    }
}
