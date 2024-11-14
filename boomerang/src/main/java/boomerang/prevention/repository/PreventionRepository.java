package boomerang.prevention.repository;

import boomerang.member.domain.Member;
import boomerang.prevention.domain.Prevention;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreventionRepository extends JpaRepository<Prevention, Long> {

    List<Prevention> findAllByMemberOrderByIdDesc(Member member);

    Optional<Prevention> findTopByMemberAndAddressOrderByIdDesc(Member member, String address);
}