package boomerang.consultation.repository;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.Schedule;
import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Page<Consultation> findAllByMentee(Member mentee, Pageable pageable);

    Page<Consultation> findAllByMentor(Mentor mentor, Pageable pageable);

    boolean existsByMenteeAndMentorAndSchedule(Member mentee, Mentor mentor, Schedule schedule);
}
