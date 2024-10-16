package boomerang.mentor.repository;

import boomerang.member.domain.Member;
import boomerang.mentor.domain.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    boolean existsByMember(Member member);
}
