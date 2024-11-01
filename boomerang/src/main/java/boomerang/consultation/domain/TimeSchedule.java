package boomerang.consultation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "time_schedule")
public class TimeSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hour;

    @ManyToOne
    @JoinColumn(name = "day_schedule")
    private DaySchedule daySchedule;

    public TimeSchedule(int hour, DaySchedule daySchedule) {
        this.hour = hour;
        this.daySchedule = daySchedule;
    }

    public TimeSchedule() {

    }
}
