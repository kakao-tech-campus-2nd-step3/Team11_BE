package boomerang.consultation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "day_schedule")
public class DaySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int day;

    @ManyToOne
    @JoinColumn(name = "month_schedule_id")
    private MonthSchedule monthSchedule;

    @JsonIgnore
    @OneToMany(mappedBy = "daySchedule", orphanRemoval = true)
    private List<TimeSchedule> timeScheduless = new ArrayList<>();

    public DaySchedule(int day, MonthSchedule monthSchedule) {
        this.day = day;
        this.monthSchedule = monthSchedule;
    }

    public DaySchedule() {

    }
}

