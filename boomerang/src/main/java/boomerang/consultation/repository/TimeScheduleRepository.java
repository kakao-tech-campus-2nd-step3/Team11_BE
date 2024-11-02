package boomerang.consultation.repository;

import boomerang.consultation.domain.TimeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeScheduleRepository extends JpaRepository<TimeSchedule, Long> {
}
