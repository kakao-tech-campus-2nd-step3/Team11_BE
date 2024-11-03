package boomerang.consultation.domain;

import boomerang.mentor.domain.Mentor;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date; // 일정이 적용될 날짜

    @ElementCollection
    @Column(length = 24)
    private List<Boolean> hourlySlots = new ArrayList<>(Collections.nCopies(24, Boolean.FALSE)); // 하루 24시간 예약 상태를 기본값 false로 초기화된 리스트

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    public Schedule(Mentor mentor, LocalDate date) {
        this.mentor = mentor;
        this.date = date;
    }

    // 일정 등록
    public void reserveSlot(int hour) {
        this.hourlySlots.set(hour, true); // 지정된 시간대의 예약 상태를 true로 설정
    }

    // 등록한 일정 취소
    public void unreserveSlot(int hour) {
        this.hourlySlots.set(hour, false);
    }



}
