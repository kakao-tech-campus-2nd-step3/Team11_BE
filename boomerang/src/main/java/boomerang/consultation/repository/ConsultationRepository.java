package boomerang.consultation.repository;

import boomerang.consultation.domain.Consultation;
import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Page<Consultation> findAllByMentee(Member mentee, Pageable pageable);

    Page<Consultation> findAllByMentor(Mentor mentor, Pageable pageable);

    boolean existsByMenteeAndMentorAndConsultationDate(Member mentee, Mentor mentor, LocalDate consultationDate);
}
