package boomerang.consultation.repository;

import boomerang.consultation.domain.DaySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayScheduleRepository extends JpaRepository<DaySchedule, Long> {
}
