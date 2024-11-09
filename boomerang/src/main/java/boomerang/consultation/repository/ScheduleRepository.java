package boomerang.consultation.repository;

import boomerang.consultation.domain.Schedule;
import boomerang.mentor.domain.Mentor;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Optional<Schedule> findByDate(LocalDate date);

    Optional<Schedule> findByMentorAndDate(Mentor mentor, LocalDate date);

    List<Schedule> findAllByMentor(Mentor mentor);
}
