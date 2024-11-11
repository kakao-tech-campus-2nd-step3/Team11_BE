package boomerang.consultation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 특정 날짜와 예약된 시간 목록을 포함하는 DaySchedule 클래스
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ScheduleDayDto {
    private String day;
    private List<ScheduleHourDto> reservedHours;

    public ScheduleDayDto(String day, List<ScheduleHourDto> reservedHours) {
        this.day = day;
        this.reservedHours = reservedHours;
    }
}
