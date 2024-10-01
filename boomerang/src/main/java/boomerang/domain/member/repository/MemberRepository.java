package boomerang.domain.member.repository;

import boomerang.domain.member.domain.Email;
import boomerang.domain.member.domain.MemberDomain;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberDomain, Long> {
    boolean existsById(Long id);
    boolean existsByEmail(Email email);
    Optional<MemberDomain> findByEmail(Email email);
}
