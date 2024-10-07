package boomerang.member.repository;

import boomerang.member.domain.Member;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsById(Long id);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
}
