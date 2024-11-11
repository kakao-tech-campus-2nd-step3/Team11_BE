package boomerang.consultation.domain;

import boomerang.global.exception.BusinessException;
import boomerang.global.response.ErrorCode;
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

    private LocalDate localDate;

    @ElementCollection
    @Column(length = 24)
    private List<Boolean> hourlySlots = new ArrayList<>(
        Collections.nCopies(24, Boolean.FALSE)); // 하루 24시간 예약 상태를 기본값 false로 초기화된 리스트

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;


    public Schedule(Mentor mentor, LocalDate localDate) {
        this.mentor = mentor;
        this.localDate = localDate;
    }

    // 상담 가능 시간으로 변경
    public void reserveHourSlot(int hour) {
        this.hourSlots.set(hour, true); // 지정된 시간대의 예약 상태를 true로 설정
    }

    // 상담 불가 시간으로 변경
    public void unreserveHourSlot(int hour) {
        this.hourSlots.set(hour, false);
    }

    public void validateHourSlot(int hour) {
        if (!hourSlots.get(hour)) {
            throw new BusinessException(ErrorCode.CONSULTATION_ALREADY_EXISTS);
        }
    }
}
