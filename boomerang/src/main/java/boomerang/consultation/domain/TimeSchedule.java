package boomerang.consultation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class TimeSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hourValue;

    @ManyToOne
    @JoinColumn(name = "daySchedule_id")
    private DaySchedule daySchedule;

    public TimeSchedule(int hour, DaySchedule daySchedule) {
        this.hourValue = hour;
        this.daySchedule = daySchedule;
    }

    public TimeSchedule() {

    }
}
