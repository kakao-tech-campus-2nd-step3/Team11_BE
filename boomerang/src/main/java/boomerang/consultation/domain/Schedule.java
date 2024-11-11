package boomerang.consultation.domain;

import boomerang.mentor.domain.Mentor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<Boolean> hourlySlots = new ArrayList<>(
        Collections.nCopies(24, Boolean.FALSE)); // 하루 24시간 예약 상태를 기본값 false로 초기화된 리스트

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultationList;

    private Integer lastReservedSlot = null; // 방금 예약한 시간대

    public Schedule(Mentor mentor, LocalDate date) {
        this.mentor = mentor;
        this.date = date;
    }

    // 상담 가능 시간으로 변경
    public void reserveSlot(int hour) {
        this.hourlySlots.set(hour, true); // 지정된 시간대의 예약 상태를 true로 설정
        if (this.lastReservedSlot != null && this.lastReservedSlot == hour) {
            this.lastReservedSlot = null; // 취소된 시간이 마지막 예약 시간대였다면 초기화
        }
    }

    // 상담 불가 시간으로 변경
    public void unreserveSlot(int hour) {
        this.hourlySlots.set(hour, false);
        this.lastReservedSlot = hour; // 방금 예약한 시간대 저장
    }


}
