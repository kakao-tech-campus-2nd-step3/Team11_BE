package boomerang.mentor.repository;

import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    Page<Mentor> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Mentor> findByIdAndIsDeletedFalse(Long id);

    Optional<Mentor> findByMemberAndIsDeletedFalse(Member member);

    Optional<Mentor> findByMember(Member member);
}
