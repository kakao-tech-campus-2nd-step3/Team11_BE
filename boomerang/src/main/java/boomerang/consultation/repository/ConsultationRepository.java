package boomerang.consultation.repository;

import boomerang.consultation.domain.Consultation;
import boomerang.consultation.domain.ConsultationStatus;
import boomerang.consultation.domain.Schedule;
import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Page<Consultation> findAllByMentee(Member mentee, Pageable pageable);

    Page<Consultation> findAllByMentor(Mentor mentor, Pageable pageable);

    boolean existsByMenteeAndMentorAndConsultationDateTime(Member mentee, Mentor mentor, LocalDateTime consultationDateTime);

    @Query("SELECT c FROM Consultation c WHERE c.consultationStatus = 'PENDING' AND c.consultationDateTime <= :now")
    List<Consultation> findPendingConsultationsForTime(LocalDateTime now);
}
