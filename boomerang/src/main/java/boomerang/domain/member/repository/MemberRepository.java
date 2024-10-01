package boomerang.domain.member.repository;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(Long id);
    boolean existsByEmail(Email email);
    Optional<Member> findByEmail(Email email);
}
