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
@Table(name = "month_schedule")
public class MonthSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int month;

    @JsonIgnore
    @OneToMany(mappedBy = "monthSchedule", orphanRemoval = true)
    private List<DaySchedule> daySchedules = new ArrayList<>();

    public MonthSchedule() {}

    public MonthSchedule(int month) {
        this.month = month;
    }
}
