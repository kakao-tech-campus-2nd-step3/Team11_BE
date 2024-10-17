package boomerang.mentor.repository;

import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    boolean existsByMember(Member member);

    Page<Mentor> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Mentor> findByIdAndIsDeletedFalse(Long id);

    Optional<Mentor> findByMemberAndIsDeletedFalse(Member member);
}
