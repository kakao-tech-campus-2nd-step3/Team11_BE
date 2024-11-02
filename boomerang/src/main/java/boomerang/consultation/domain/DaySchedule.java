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
public class DaySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int dayValue;

    @ManyToOne
    @JoinColumn(name = "monthSchedule_id")
    private MonthSchedule monthSchedule;

    @JsonIgnore
    @OneToMany(mappedBy = "daySchedule", orphanRemoval = true)
    private List<TimeSchedule> timeScheduless = new ArrayList<>();

    public DaySchedule(int day, MonthSchedule monthSchedule) {
        this.dayValue = day;
        this.monthSchedule = monthSchedule;
    }

    public DaySchedule() {

    }
}

