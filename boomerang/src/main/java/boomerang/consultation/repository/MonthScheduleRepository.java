package boomerang.consultation.repository;

import boomerang.consultation.domain.MonthSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthScheduleRepository extends JpaRepository<MonthSchedule, Long> {

}
