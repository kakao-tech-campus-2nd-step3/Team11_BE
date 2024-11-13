package boomerang.prevention.repository;

import boomerang.member.domain.Member;
import boomerang.prevention.domain.Prevention;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreventionRepository extends JpaRepository<Prevention, Long> {

    Optional<Prevention> findByMember(Member member);

    boolean existsByMember(Member member);
}